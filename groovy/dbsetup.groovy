@Grab('mysql:mysql-connector-java:5.1.17')
@GrabConfig(systemClassLoader=true)
 
import groovy.sql.Sql


def cli = new CliBuilder( usage: 'groovy dbsetup.groovy -h -s sql -u username -pw password')
cli.h(longOpt:'help', 'show usage information')
cli.s(argName:'sql', longOpt:'sqlfile', args:1, required:true, type:GString, 'sqlfile')
cli.u(argName:'username', longOpt:'username', args:1, required:true, type:GString, 'username')
cli.pw(argName:'password', longOpt:'password', args:1, required:false, type:GString, 'password')

def opt = cli.parse(args)
if (!opt) {
    println 'Please specify start parameters!'
    return
}

def username = opt.u
def password = opt.pw == false ? "" : opt.pw
println "Db password: $password"
def script = opt.s

def driver = "com.mysql.jdbc.Driver"
def sql = Sql.newInstance("jdbc:mysql://localhost:3306", username, password, driver)


println "run $script"

String sqlString = new File(script).eachLine {
    sql.execute(it)
}