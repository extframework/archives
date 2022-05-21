module yakclient.archives.mixin {
    requires kotlin.stdlib;
    requires transitive yakclient.archives;

    exports net.yakclient.archives.api.mixin;
//    exports net.yakclient.bmu.api.mixin.annotations;
}