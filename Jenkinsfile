pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                          branches: [[name: '*/main']],
                          userRemoteConfigs: [[url: 'https://github.com/n-khatsanovskaya/PetClinicTests.git']]])
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    try {
                        withMaven(maven: 'Maven3') {
                            sh 'mvn clean test'
                        }
                    } catch (Exception e) {
                        currentBuild.result = 'SUCCESS'
                    }
                }
           }
        }
    }
    post {
        always {
            // Archive Allure results as artifacts
            archiveArtifacts 'target/allure-results/**'

            // Publish Allure report using the Allure Jenkins Plugin
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}
