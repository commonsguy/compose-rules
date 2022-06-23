package com.twitter.rules.ktlint.compose

import com.pinterest.ktlint.test.KtLintAssertThat.Companion.assertThat
import com.pinterest.ktlint.test.LintViolation
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class ComposeModifierMissingCheckTest {

    private val modifierRuleAssertThat = ComposeModifierMissingCheck().assertThat()

    @Test
    fun `errors when a Composable has a layout inside and it doesn't have a modifier`() {
        @Language("kotlin")
        val code =
            """
                @Composable
                fun Something() {
                    Row {
                    }
                }
                @Composable
                fun Something() {
                    Column(modifier = Modifier.fillMaxSize()) {
                    }
                }
                @Composable
                fun Something(): Unit {
                    SomethingElse {
                        Box(modifier = Modifier.fillMaxSize()) {
                        }
                    }
                }
                @Composable
                fun Something(modifier: Modifier = Modifier) {
                    Row {
                        Text("Hi!")
                    }
                }
            """.trimIndent()

        modifierRuleAssertThat(code).hasLintViolationsWithoutAutoCorrect(
            LintViolation(
                line = 2,
                col = 5,
                detail = ComposeModifierMissingCheck.MissingModifierContentComposable,
            ),
            LintViolation(
                line = 7,
                col = 5,
                detail = ComposeModifierMissingCheck.MissingModifierContentComposable,
            ),
            LintViolation(
                line = 12,
                col = 5,
                detail = ComposeModifierMissingCheck.MissingModifierContentComposable,
            )
        )
    }

    @Test
    fun `errors when a Composable without modifiers has a Composable inside with a modifier`() {
        @Language("kotlin")
        val code =
            """
                @Composable
                fun Something() {
                    Whatever(modifier = Modifier.fillMaxSize()) {
                    }
                }
                @Composable
                fun Something(): Unit {
                    SomethingElse {
                        Whatever(modifier = Modifier.fillMaxSize()) {
                        }
                    }
                }
            """.trimIndent()

        modifierRuleAssertThat(code).hasLintViolationsWithoutAutoCorrect(
            LintViolation(
                line = 2,
                col = 5,
                detail = ComposeModifierMissingCheck.MissingModifierContentComposable,
            ),
            LintViolation(
                line = 7,
                col = 5,
                detail = ComposeModifierMissingCheck.MissingModifierContentComposable,
            )
        )
    }

    @Test
    fun `errors when a Composable has modifiers but without default values, and is able to auto fixing it`() {
        @Language("kotlin")
        val composableCode = """
                @Composable
                fun Something(modifier: Modifier) {
                    Row(modifier = modifier) {
                    }
                }
        """.trimIndent()

        modifierRuleAssertThat(composableCode)
            .hasLintViolation(
                line = 2,
                col = 15,
                detail = ComposeModifierMissingCheck.MissingModifierDefaultParam,
            )
            .isFormattedAs(
                """
                @Composable
                fun Something(modifier: Modifier = Modifier) {
                    Row(modifier = modifier) {
                    }
                }
            """.trimIndent()
            )
    }

    @Test
    fun `passes when a Composable has modifiers with defaults`() {
        @Language("kotlin")
        val code =
            """
                @Composable
                fun Something(modifier: Modifier = Modifier) {
                    Row(modifier = modifier) {
                    }
                }
                @Composable
                fun Something(modifier: Modifier = Modifier.fillMaxSize()) {
                    Row(modifier = modifier) {
                    }
                }
                @Composable
                fun Something(modifier: Modifier = SomeOtherValueFromSomeConstant) {
                    Row(modifier = modifier) {
                    }
                }
            """.trimIndent()
        modifierRuleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `non-public visibility Composables are ignored`() {
        @Language("kotlin")
        val code =
            """
                @Composable
                private fun Something() {
                    Row {
                    }
                }
                @Composable
                protected fun Something() {
                    Column(modifier = Modifier.fillMaxSize()) {
                    }
                }
                @Composable
                internal fun Something() {
                    SomethingElse {
                        Box(modifier = Modifier.fillMaxSize()) {
                        }
                    }
                }
                @Composable
                private fun Something() {
                    Whatever(modifier = Modifier.fillMaxSize()) {
                    }
                }
            """.trimIndent()
        modifierRuleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `interface Composables are ignored`() {
        @Language("kotlin")
        val code =
            """
                interface MyInterface {
                    @Composable
                    fun Something() {
                        Row {
                        }
                    }

                    @Composable
                    fun Something() {
                        Column(modifier = Modifier.fillMaxSize()) {
                        }
                    }
                }
            """.trimIndent()
        modifierRuleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `overridden Composables are ignored`() {
        @Language("kotlin")
        val code =
            """
                @Composable
                override fun Content() {
                    Row {
                    }
                }
                @Composable
                override fun TwitterContent() {
                    Row {
                    }
                }
                @Composable
                override fun ModalContent() {
                    Row {
                    }
                }
            """.trimIndent()
        modifierRuleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `Composables that return a type that is not Unit shouldn't be processed`() {
        @Language("kotlin")
        val code =
            """
                @Composable
                fun Something(): Int {
                    Row {
                    }
                }
            """.trimIndent()
        modifierRuleAssertThat(code).hasNoLintViolations()
    }
}
