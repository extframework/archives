package net.yakclient.verstionadapter;

import net.yakclient.mixin.api.versionadapter.ClassVersion;
import net.yakclient.mixin.api.versionadapter.VersionManager;
import net.yakclient.mixin.api.versionadapter.VersionMapping;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AdapterTests {
    @Test
    public void testVersionAdapter() throws IOException, ReflectiveOperationException {
        final VersionMapping.MappingsBuilder.VersionsBuilder mapping = new VersionMapping.MappingsBuilder("org.example").clsTargets("IDENTIFIER");
        mapping.version("1",
                new ClassVersion.ClassVersionBuilder("net.yakclient.mixin.registry.MixinRegistry")
                        .buildMethods()
                        .add("1st", "declaredMethod(Ljava.lang.String;Ljava.lang.String;[Ljava.lang.Class;)B")
                        .add("2nd", "asdf2(BB)V").build()
                        .buildFields().add("1st", "configuration").build().build()).build().build();
        VersionManager.apply(mapping.build().build(), "1");
        System.out.println(VersionManager.clazz("org.example", "IDENTIFIER"));
        System.out.println(VersionManager.method("org.example", "IDENTIFIER", "2nd"));
        System.out.println(VersionManager.field("org.example", "IDENTIFIER", "1st"));


    }
}
