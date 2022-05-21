package net.yakclient.archives.mixin.annotations

import java.io.InputStream

///**
// * `Mixer` marks a class as a Mixer. All mixer classes should
// * be abstract.
// *
// * An example;
// *
// * ```
// * @Mixer("org.example.TestCase")
// * abstract class MixerClass { /*...*/ }
// * ```
// *
// * @see Injection
// *
// * @author Durgan McBroom
// */
//@Retention(AnnotationRetention.RUNTIME)
//@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
//public annotation class Mixer(
//    /**
//     * `Mixer#type()` provides the class that will
//     * be mixed to.
//     *
//     * @return returns the class that will be mixed into.
//     */
//    val value: String,
//)