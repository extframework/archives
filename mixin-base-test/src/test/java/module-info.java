module mixin.base.test {
    requires transitive mixin.base;

    requires org.junit.jupiter.api;
    requires org.opentest4j;
    requires org.junit.platform.commons;
    requires org.apiguardian.api;
}