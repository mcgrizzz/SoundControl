pipeline {
    agent any
    tools{
        maven 'maven 3'
        jdk 'java 8'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                git branch
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
                find ~/.m2/repository -name _maven.repositories -exec rm -v {} +;
                '''
            }
        }

        stage ('Build') {
            steps {
                sh '''
                mvn install -e
                '''
            }
        }
      stage('Artifact') {
        steps {
          archiveArtifacts '*/target/*.jar'
          mvn -N help:effective-pom -Doutput=result.xml
            
            script {
              def pom = readMavenPom file: 'result.xml'
              sh " ~/workingDir/scripts/soundcontrol_gitrelease.sh ${pom.version} ${WORKSPACE}" 
           }
          
        }
      }
    }
}
