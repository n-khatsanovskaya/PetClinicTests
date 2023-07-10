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
                        success("There were test failures.")
                    }
                }
           }
        }

        stage('Generate Allure Report') {
            steps {
                // Generate Allure report
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }

        stage('Publish Allure Report') {
            steps {
                // Publish Allure report as a Jenkins artifact
                archiveArtifacts 'target/allure-report'
            }
        }
    }
}
