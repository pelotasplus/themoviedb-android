package pl.pelotasplus.themoviedb.demo

import spock.lang.Specification

class DummySpec extends Specification {
    def "setup"() {
    }

    def "dummy test"() {
        expect:
        1 == 1

        and:
        1 != 2
    }
}
