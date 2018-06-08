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
                find ~/.m2  -name "*.lastUpdated" -exec grep -q "Could not transfer" {} +;
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
          archiveArtifacts 'target/*.jar'
          
        }
      }
    }
}
