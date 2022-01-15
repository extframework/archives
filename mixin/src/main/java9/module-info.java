module yakclient.bmu.mixin {
    requires kotlin.stdlib;
    requires transitive yakclient.bmu.api;

    exports net.yakclient.bmu.api.mixin;
}