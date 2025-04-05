pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/myrepo/microservices.git'
            }
        }

        stage('Build Services') {
            steps {
                parallel (
                    authService: { sh 'mvn clean package -DskipTests -f auth-service/pom.xml' },
                    inventoryService: { sh 'mvn clean package -DskipTests -f inventory-service/pom.xml' },
                    vehicleService: { sh 'mvn clean package -DskipTests -f vehicle-service/pom.xml' }
                )
            }
        }

        stage('Docker Build & Push') {
            steps {
                parallel (
                    authService: {
                        sh 'docker build -t myrepo/auth-service:latest ./auth-service'
                        sh 'docker push myrepo/auth-service:latest'
                    },
                    inventoryService: {
                        sh 'docker build -t myrepo/inventory-service:latest ./inventory-service'
                        sh 'docker push myrepo/inventory-service:latest'
                    },
                    vehicleService: {
                        sh 'docker build -t myrepo/vehicle-service:latest ./vehicle-service'
                        sh 'docker push myrepo/vehicle-service:latest'
                    }
                )
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh 'kubectl apply -f ops/deployments/auth-service-deployment.yaml'
                sh 'kubectl apply -f ops/deployments/inventory-service-deployment.yaml'
                sh 'kubectl apply -f ops/deployments/vehicle-service-deployment.yaml'
            }
        }
    }

    post {
        success {
            echo "All services deployed successfully!"
        }
        failure {
            echo "Deployment failed!"
        }
    }
}
