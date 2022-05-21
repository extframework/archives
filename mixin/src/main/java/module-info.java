module yakclient.bmu.mixin {
    requires kotlin.stdlib;
    requires transitive yakclient.archives.api;

    exports net.yakclient.bmu.api.mixin;
//    exports net.yakclient.bmu.api.mixin.annotations;
}