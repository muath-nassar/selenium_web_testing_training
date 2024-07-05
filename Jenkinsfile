pipeline {
    agent any
    tools {
        // Ensure the correct JDK is installed and available
        jdk 'JDK17'
        maven 'Maven3'
    }
    stages {
        stage('Checkout') {
            steps {
                // Checkout code from the repository
                checkout scm
            }
        }
        stage('Checkstyle') {
                    steps {
                        // Run Checkstyle
                        sh 'mvn checkstyle:check'
                    }
                }
    }
    post {
        always {
            // Archive the Checkstyle results
            archiveArtifacts artifacts: '**/target/checkstyle-result.xml', allowEmptyArchive: true
        }
        success {
            // Notify success
            echo 'Build completed successfully!'
        }
        failure {
            // Notify failure
            echo 'Build failed!'
        }
    }
}
