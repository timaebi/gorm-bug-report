package bug.report

class User {
    String username

    static belongsTo = [organisation: Organisation]

    static constraints = {
    }
}
