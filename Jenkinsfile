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

        stage('Build') {
            steps {
                withMaven(maven: 'Maven3') {
                        sh 'mvn clean test'
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                // Generate Allure report
                sh 'allure generate target/allure-results --clean -o target/allure-report'
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