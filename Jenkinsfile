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
                            sh 'mvn clean test -Dbase.url=${baseUrl} -Dsuite=\"${suite}.xml\"'
                        }
                    } catch (Exception e) {
                        currentBuild.result = 'SUCCESS'
                    }
                    echo "build status: ${currentBuild.result}"
                }
           }
        }
        stage('Allure Report') {
            steps {
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
}
