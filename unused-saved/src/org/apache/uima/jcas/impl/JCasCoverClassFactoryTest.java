/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.uima.cas.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.test.junit_extension.JUnitExtension;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.junit.jupiter.api.Test;

public class JCasCoverClassFactoryTest {

    @Test
    public void testCreateJCasCoverClass() throws InvalidXMLException, IOException, ResourceInitializationException {
    File file = JUnitExtension.getFile("JCasGen/typeSystemAllKinds.xml");
    TypeSystemDescription tsDesc = UIMAFramework.getXMLParser().parseTypeSystemDescription(
            new XMLInputSource(file));
   
    CAS cas = CasCreationUtils.createCas(tsDesc, null, null);
    
    JCasCoverClassFactory jcf = new JCasCoverClassFactory();
    
    byte[] r = jcf.createJCasCoverClass((TypeImpl) cas.getTypeSystem().getType("pkg.sample.name.All"));

    Path root = Paths.get(".");  // should resolve to the project path
    Path dir = root.resolve("temp/test/JCasGen");
    dir.toFile().mkdirs();
    Files.write(dir.resolve("testOutputAllKinds.class"), r);
    
    System.out.println("debug: generated byte array");
  }

}
