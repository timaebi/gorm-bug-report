package bug.report

class Organisation {
    String name

    static hasMany = [members: User]

    static constraints = {
    }
}
