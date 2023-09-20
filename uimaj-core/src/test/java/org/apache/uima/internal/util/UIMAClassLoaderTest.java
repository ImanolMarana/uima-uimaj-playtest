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

package org.apache.uima.internal.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.ResourceManager;
import org.apache.uima.test.junit_extension.JUnitExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * UIMA ClassLoader test
 * 
 */
public class UIMAClassLoaderTest {

  private String testClassPath;

  @BeforeEach
  public void setUp() throws Exception {

    testClassPath = JUnitExtension.getFile("ClassLoaderTest/classLoadingTest.jar")
            .getAbsolutePath();
  }

  @Test
  public void testSimpleRsrcMgrCLassLoaderCreation() throws Exception {
    ResourceManager rsrcMgr = UIMAFramework.newDefaultResourceManager();

    assertThat(rsrcMgr.getExtensionClassLoader()) //
            .as("Freshly created resource manager has no extension classloader") //
            .isNull();

    rsrcMgr.setExtensionClassPath("../this/is/a/simple/test.jar", false);

    assertThat(rsrcMgr.getExtensionClassLoader()) //
            .as("After setting an extension classpath, there is an extension classloader") //
            .isInstanceOf(UIMAClassLoader.class);

    UIMAClassLoader cl = (UIMAClassLoader) rsrcMgr.getExtensionClassLoader();
    Object o = cl.getClassLoadingLockForTesting("someString");
    Object o2 = cl.getClassLoadingLockForTesting("s2");
    assertTrue(o != o2);
    assertTrue(cl != o);
  }

  @Test
  public void testAdvancedRsrcMgrCLassLoaderCreation() throws Exception {
    ResourceManager rsrcMgr = UIMAFramework.newDefaultResourceManager();

    assertNull(rsrcMgr.getExtensionClassLoader());

    rsrcMgr.setExtensionClassPath("../this/is/a/simple/test.jar", true);

    assertNotNull(rsrcMgr.getExtensionClassLoader());

  }

  @Test
  public void testSimpleClassloadingSampleString() throws Exception {
    UIMAClassLoader cl = new UIMAClassLoader(testClassPath, this.getClass().getClassLoader());
    Class testClass = null;

    testClass = cl.loadClass("org.apache.uima.internal.util.ClassloadingTestClass");

    assertNotNull(testClass);
    assertEquals(cl, testClass.getClassLoader());

    testClass = cl.loadClass("org.apache.uima.flow.impl.AnalysisSequenceCapabilityNode");

    assertNotNull(testClass);
    assertEquals(this.getClass().getClassLoader(), testClass.getClassLoader());
  }

  @Test
  public void testParallelClassLoading() throws Exception {
    final UIMAClassLoader cl = new UIMAClassLoader(testClassPath,
            this.getClass().getClassLoader());
    final Class<?>[] loadedClasses = new Class<?>[Misc.numberOfCores];

    MultiThreadUtils.Run2isb callable = new MultiThreadUtils.Run2isb() {
      @Override
      public void call(int threadNumber, int repeatNumber, StringBuilder sb) throws Exception {
        loadedClasses[threadNumber] = cl
                .loadClass("org.apache.uima.internal.util.ClassloadingTestClass");
      }
    };

    MultiThreadUtils.tstMultiThread("MultiThreadLoading", Misc.numberOfCores, 1, callable,
            MultiThreadUtils.emptyReset);
    Class<?> c = loadedClasses[0];
    for (int i = 1; i < Misc.numberOfCores; i++) {
      assertEquals(c, loadedClasses[i]);
    }
  }

  @Test
  public void testSimpleClassloadingSampleURL() throws Exception {
    URL[] urlClasspath = new URL[] { new File(testClassPath).toURL() };
    UIMAClassLoader cl = new UIMAClassLoader(urlClasspath, this.getClass().getClassLoader());
    Class testClass = null;

    testClass = cl.loadClass("org.apache.uima.internal.util.ClassloadingTestClass");

    assertNotNull(testClass);
    assertEquals(cl, testClass.getClassLoader());

    testClass = cl.loadClass("org.apache.uima.flow.impl.AnalysisSequenceCapabilityNode");

    assertNotNull(testClass);
    assertEquals(this.getClass().getClassLoader(), testClass.getClassLoader());
  }

  @Test
  public void testAdvancedClassloadingSampleString() throws Exception {
    UIMAClassLoader cl = new UIMAClassLoader(testClassPath, this.getClass().getClassLoader());
    Class testClass = null;

    testClass = cl.loadClass("org.apache.uima.internal.util.ClassloadingTestClass");

    assertNotNull(testClass);
    assertEquals(cl, testClass.getClassLoader());

    testClass = cl.loadClass("org.apache.uima.flow.impl.AnalysisSequenceCapabilityNode");

    assertNotNull(testClass);
    assertEquals(this.getClass().getClassLoader(), testClass.getClassLoader());
  }

  @Test
  public void testAdvancedClassloadingSampleURL() throws Exception {
    URL[] urlClasspath = new URL[] { new File(testClassPath).toURL() };
    UIMAClassLoader cl = new UIMAClassLoader(urlClasspath, this.getClass().getClassLoader());
    Class testClass = null;

    testClass = cl.loadClass("org.apache.uima.internal.util.ClassloadingTestClass");

    assertNotNull(testClass);
    assertEquals(cl, testClass.getClassLoader());

    testClass = cl.loadClass("org.apache.uima.flow.impl.AnalysisSequenceCapabilityNode");

    assertNotNull(testClass);
    assertEquals(this.getClass().getClassLoader(), testClass.getClassLoader());

  }
}
