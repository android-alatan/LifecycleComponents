package io.androidalatan.compose.local.ksp

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.androidalatan.compose.local.ksp.api.ProvidedComposeLocal
import java.io.OutputStreamWriter

class ComposeLocalProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    @Suppress("unused") private val moduleName: String
) : SymbolProcessor {

    private val properties = mutableListOf<KSPropertyDeclaration>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols =
            resolver.getSymbolsWithAnnotation(ProvidedComposeLocal::class.java.canonicalName)

        symbols
            .filter { ksAnnotated ->
                ksAnnotated is KSPropertyDeclaration && ksAnnotated.validate()
            }
            .forEach { ksAnnotated ->
                ksAnnotated.accept(ProvidedComposeLocalVisitor(codeGenerator, logger), Unit)
                properties.add(ksAnnotated as KSPropertyDeclaration)
            }

        return emptyList()
    }

    override fun finish() {

        if (properties.isEmpty()) return

        val owners = properties.mapNotNull { it.parentDeclaration }
            .distinctBy { it.qualifiedName?.asString() ?: "" }

        owners.forEach { owner ->
            generateOwnerComposeProviderLocal(owner, properties.filter { it.parentDeclaration == owner })
        }
    }

    private fun generateOwnerComposeProviderLocal(owner: KSDeclaration, properties: List<KSPropertyDeclaration>) {

        val ownerClassName = owner.toClassName()

        val fileSpecBuilder = FileSpec.builder(
            ownerClassName.packageName,
            "${ownerClassName.simpleName}_CompositionLocalProvider"
        )
            .addImport(LOCAL_LIFECYCLE_OWNER.packageName, LOCAL_LIFECYCLE_OWNER.simpleName)
            .addImport(COMPOSITION_LOCAL_PROVIDER.packageName, COMPOSITION_LOCAL_PROVIDER.simpleName)
            .addImport(ownerClassName.packageName, ownerClassName.simpleName)

        val composableSpec =
            AnnotationSpec.builder(COMPOSABLE)
                .build()

        val contentParameterSpec = composableContentSpec(composableSpec)

        val array = properties.filter { it.parentDeclaration == owner }
            .map { it.toClassName() }
            .map { propertyClassName ->
                "${propertyClassName.simpleName}ProviderValue(${propertyClassName.simpleName})"
            }
            .toTypedArray()

        fileSpecBuilder.addFunction(
            FunSpec.builder("ComposeLocalProvider")
                .addAnnotation(composableSpec)
                .addModifiers(KModifier.INTERNAL)
                .receiver(ownerClassName)
                .addParameter(contentParameterSpec)
                .addStatement(
                    "${COMPOSITION_LOCAL_PROVIDER.simpleName}(${
                        array
                            .map { "\n%L" }
                            .joinToString { it }
                    }, \ncontent = %N\n)",
                    *array,
                    contentParameterSpec
                )
                .build()
        )
            .build()

        val fileSpec = fileSpecBuilder.build()
        val outputStream = codeGenerator.createNewFile(
            Dependencies(
                false,
                owner.containingFile!!
            ),
            owner.packageName.asString(),
            fileSpec.name
        )
        OutputStreamWriter(outputStream, "UTF-8")
            .use { fileSpec.writeTo(it) }
    }

    private fun isLifecycleOwner(owner: KSDeclaration): Boolean {
        val declaration: KSClassDeclaration = owner as KSClassDeclaration
        return declaration.getAllSuperTypes()
            .any {
                it.declaration.qualifiedName?.asString() == LIFECYCLE_OWNER.canonicalName
            }
    }

    private fun ownerParamSpec(ownerClassName: ClassName) = ParameterSpec.builder(
        "owner",
        ownerClassName
    )
        .build()

    private fun composableContentSpec(composableSpec: AnnotationSpec) = ParameterSpec.builder(
        "content",
        LambdaTypeName.get(
            returnType = ClassName(
                Unit::class.java.packageName,
                Unit::class.java.simpleName
            )
        )
            .copy(annotations = listOf(composableSpec))
    )
        .build()

    class ProvidedComposeLocalVisitor(
        private val codeGenerator: CodeGenerator,
        @Suppress("unused") private val logger: KSPLogger
    ) : KSVisitorVoid() {

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {

            val ownerDeclaration = property.parentDeclaration ?: return

            val ownerClassName = ownerDeclaration.toClassName()
            val propertyClassName: ClassName = property.type.resolve().declaration.toClassName()

            val variableName = "Local${propertyClassName.simpleName}"
            val propertyName = property.simpleName.asString()
            val fileSpec = FileSpec.builder(
                ownerClassName.packageName,
                "${ownerClassName.simpleName}_$propertyName"
            )
                .addImport(COMPOSITION_LOCAL_OF.packageName, COMPOSITION_LOCAL_OF.simpleName)
                .addProperty(
                    PropertySpec.Companion.builder(
                        variableName,
                        PROVIDED_COMPOSE_LOCAL.parameterizedBy(propertyClassName),
                        KModifier.PRIVATE
                    )
                        .initializer(buildCodeBlock {
                            add(
                                "${COMPOSITION_LOCAL_OF.simpleName}<%T> { error(%S) }",
                                propertyClassName,
                                "$variableName isn't provided"
                            )
                        })
                        .build()
                )
                .addFunction(
                    FunSpec.builder("${propertyName}ProviderValue")
                        .addModifiers(KModifier.INTERNAL)
                        .addParameter(propertyName, propertyClassName)
                        .addStatement(
                            "return %L·provides·%L",
                            variableName,
                            propertyName
                        )
                        .build()
                )
                .addFunction(
                    FunSpec.builder(propertyName)
                        .addModifiers(KModifier.INTERNAL)
                        .addAnnotation(COMPOSABLE)
                        .addStatement("return %L.current", variableName)
                        .build()
                )
                .build()

            val outputStream = codeGenerator.createNewFile(
                Dependencies(
                    false,
                    ownerDeclaration.containingFile!!
                ),
                fileSpec.packageName,
                fileSpec.name
            )

            OutputStreamWriter(outputStream, "UTF-8")
                .use { fileSpec.writeTo(it) }
        }
    }

    companion object {
        val COMPOSABLE = ClassName("androidx.compose.runtime", "Composable")
        val PROVIDED_COMPOSE_LOCAL =
            ClassName("androidx.compose.runtime", "ProvidableCompositionLocal")
        val LOCAL_LIFECYCLE_OWNER = ClassName("androidx.compose.ui.platform", "LocalLifecycleOwner")
        val COMPOSITION_LOCAL_PROVIDER =
            ClassName("androidx.compose.runtime", "CompositionLocalProvider")
        val COMPOSITION_LOCAL_OF = ClassName("androidx.compose.runtime", "compositionLocalOf")
        val LIFECYCLE_OWNER = ClassName("androidx.lifecycle", "LifecycleOwner")
    }
}