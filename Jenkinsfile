// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements. See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership. The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License. You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied. See the License for the
// specific language governing permissions and limitations
// under the License.

@Library('uima-build-jenkins-shared-library@feature/UIMA-6437-Allow-publishing-HTML-documentation-from-Jenkinsfile') _

defaultPipeline {
  // The Eclipse libraries that our plugins depend unfortunately on required Java 11
  jdk = 'jdk_11_latest'
  extraMavenArguments = '-Pjacoco,pmd,run-rat-report'
  documentation = [[
    allowMissing: false,
    alwaysLinkToLastBuild: true,
    keepAll: false,
    reportDir: 'uima-doc-v3-maintainers-guide/target/site/d',
    includes: '**/*',
    reportFiles: 'version_3_maintainers_guide.html',
    reportName: 'Maintainers Guide',
    reportTitles: 'Maintainers Guide'
  ]]
}
