#include <iostream>
#include <iostream>
#include <cstdio>
#include <cstdlib>

std::string data;
FILE * stream;
const int MAX_BUFFER = 256;
std::string java = R"(/Library/Java/JavaVirtualMachines/jdk-10.0.1.jdk/Contents/Home/bin/java "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=57011:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Users/esie/Library/Mobile Documents/com~apple~CloudDocs/Documents/deplom/target/classes:/Users/esie/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.2.0/junit-jupiter-api-5.2.0.jar:/Users/esie/.m2/repository/org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar:/Users/esie/.m2/repository/org/opentest4j/opentest4j/1.1.0/opentest4j-1.1.0.jar:/Users/esie/.m2/repository/org/junit/platform/junit-platform-commons/1.2.0/junit-platform-commons-1.2.0.jar" com.esie.Main)";

std::string get_std_command(std::string command)
{
    char buffer[MAX_BUFFER];
    command.append(" 2>&1");
    stream = popen(command.c_str(), "r");
    if (stream)
    {
        while (!feof(stream))
            if (fgets(buffer, MAX_BUFFER, stream) != nullptr)
                data.append(buffer);
        pclose(stream);
    }
    return data;
}

int main ()
{
    std::string com = get_std_command(java);
    std::cout << "Command: " << com << std::endl;
    return 0;
}