import net.yakclient.archives.ArchiveFinder;
import net.yakclient.archives.ArchiveResolver;
import net.yakclient.archives.jpm.JpmFinder;
import net.yakclient.archives.jpm.JpmResolver;
import net.yakclient.archives.zip.ZipFinder;
import net.yakclient.archives.zip.ZipResolver;

module yakclient.archives {
    requires transitive org.objectweb.asm;
    requires transitive org.objectweb.asm.tree;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires java.instrument;
    requires transitive org.objectweb.asm.util;
    requires jdk.attach;
    requires yakclient.common.util;

    exports net.yakclient.archives;
    exports net.yakclient.archives.extension;
    exports net.yakclient.archives.transform;
    exports net.yakclient.archives.jpm;
    exports net.yakclient.archives.zip;

    uses ArchiveResolver;
    uses ArchiveFinder;

    provides ArchiveResolver with JpmResolver, ZipResolver;
    provides ArchiveFinder with JpmFinder, ZipFinder;
}