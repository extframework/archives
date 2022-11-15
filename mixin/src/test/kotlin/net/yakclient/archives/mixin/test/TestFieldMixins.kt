package net.yakclient.archives.mixin.test

import net.yakclient.archives.mixin.FieldInjection
import net.yakclient.archives.mixin.FieldInjectionData
import kotlin.test.Test

class TestFieldMixins {
    @Test
    fun `Mix fields`() {
        val config = FieldInjection.apply(
            FieldInjectionData(
                name = "testField",
                type = "Ljava/lang/String;",
            )
        )

        val c = MixFieldsTo::class.java.transform(config)

        println(c.fields.map { it.name })
        check(c.fields.isNotEmpty())
    }
}

class MixFieldsTo {

}