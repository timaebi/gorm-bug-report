import bug.report.Organisation
import bug.report.User
import grails.gorm.DetachedCriteria
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.core.convert.ConverterNotFoundException
import spock.lang.Specification

@Integration
@Rollback
class AssociationSubCriteriaSpec extends Specification {

    def setup() {
        def orgA = new Organisation(name: 'A')
        def orgB = new Organisation(name: 'B')
        def orgZ = new Organisation(name: 'Z')

        def membersA = [
                new User(username: 'User A1'),
                new User(username: 'User A2'),
                new User(username: 'User A3'),
        ]
        membersA.forEach { orgA.addToMembers(it) }

        def membersB = [
                new User(username: 'User B1'),
                new User(username: 'User B2'),
        ]
        membersB.forEach { orgB.addToMembers(it) }

        def membersZ = [
                new User(username: 'User Z1'),
                new User(username: 'User Z2'),
        ]
        membersZ.forEach { orgZ.addToMembers(it) }

        [orgA, orgB, orgZ]*.save(failOnError: true)
    }

    def "test run detached criteria with association criteria multiple times"() {
        DetachedCriteria<User> criteria = User.where {
            'in'('organisation', Organisation.where { name == 'A' || name == 'B' }.id())
        }
        when:
        def firstResult = criteria.list()
        def secondResult = criteria.list()

        then:
        notThrown(ConverterNotFoundException)
        firstResult == secondResult
    }
}
