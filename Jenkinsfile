pipeline {
    agent any
    
    parameters {
        string(name: 'app', defaultValue: 'vblessimg/vbless', description: 'vbless docker image')
    }

    stages {
        stage('Build') {
            steps {
            		sh '''
                 mvn package dockerfile:build -DskipTests;
                 docker login --username=$dockeruserid --password=$dockeruserpw
                 docker tag vbless-ui chidanandapati/vbless-server:v1;
                 docker push chidanandapati/vbless-ui:v1;
                 '''
            }
        }
        stage('Test') {
            steps {
                sh '''
                mvn test
                '''
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                export KUBERNETES_MASTER=http://127.0.0.1:8001
                kubectl apply -f server-deployment.yaml
                kubectl apply -f server-service.yaml
                kubectl rollout status deployment/vbless-server 
                '''
            }
        }
    }
}
