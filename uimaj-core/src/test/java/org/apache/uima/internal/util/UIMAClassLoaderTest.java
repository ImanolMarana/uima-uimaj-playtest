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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Method;
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

    this.testClassPath = JUnitExtension.getFile("ClassLoaderTest/classLoadingTest.jar")
            .getAbsolutePath();
  }

  @Test
  public void testSimpleRsrcMgrCLassLoaderCreation() throws Exception {
    ResourceManager rsrcMgr = UIMAFramework.newDefaultResourceManager();

    assertNull(rsrcMgr.getExtensionClassLoader());

    rsrcMgr.setExtensionClassPath("../this/is/a/simple/test.jar", false);
    ClassLoader cl = rsrcMgr.getExtensionClassLoader();
    assertNotNull(cl);
    // assertTrue(cl != cl.getClassLoadingLock("Aclass"));
    Class classOfLoader = cl.getClass().getSuperclass();
    while (!(classOfLoader.getName().equals("java.lang.ClassLoader"))) {
      classOfLoader = classOfLoader.getSuperclass();
    }
    if (!Misc.isJava9ea) { // skip for java 9
      Method m = classOfLoader.getDeclaredMethod("getClassLoadingLock", String.class);
      m.setAccessible(true);
      Object o = m.invoke(cl, "someString");
      Object o2 = m.invoke(cl, "s2");
      assertTrue(o != o2);
      assertTrue(cl != o);
    }
  }

  @org.junit.jupiter.api.Test
  public void testAdvancedRsrcMgrCLassLoaderCreation() throws Exception {
    ResourceManager rsrcMgr = UIMAFramework.newDefaultResourceManager();

    assertNull(rsrcMgr.getExtensionClassLoader());

    rsrcMgr.setExtensionClassPath("../this/is/a/simple/test.jar", true);

    assertNotNull(rsrcMgr.getExtensionClassLoader());

  }

  @org.junit.jupiter.api.Test
  public void testSimpleClassloadingSampleString() throws Exception {
    UIMAClassLoader cl = new UIMAClassLoader(this.testClassPath, this.getClass().getClassLoader());
    Class testClass = null;

    testClass = cl.loadClass("org.apache.uima.internal.util.ClassloadingTestClass");

    assertNotNull(testClass);
    assertEquals(cl, testClass.getClassLoader());

    testClass = cl.loadClass("org.apache.uima.flow.impl.AnalysisSequenceCapabilityNode");

    assertNotNull(testClass);
    assertEquals(this.getClass().getClassLoader(), testClass.getClassLoader());
  }

  @org.junit.jupiter.api.Test
  public void testParallelClassLoading() throws Exception {
    final UIMAClassLoader cl = new UIMAClassLoader(this.testClassPath,
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

  @org.junit.jupiter.api.Test
  public void testSimpleClassloadingSampleURL() throws Exception {
    URL[] urlClasspath = new URL[] { new File(this.testClassPath).toURL() };
    UIMAClassLoader cl = new UIMAClassLoader(urlClasspath, this.getClass().getClassLoader());
    Class testClass = null;

    testClass = cl.loadClass("org.apache.uima.internal.util.ClassloadingTestClass");

    assertNotNull(testClass);
    assertEquals(cl, testClass.getClassLoader());

    testClass = cl.loadClass("org.apache.uima.flow.impl.AnalysisSequenceCapabilityNode");

    assertNotNull(testClass);
    assertEquals(this.getClass().getClassLoader(), testClass.getClassLoader());
  }

  @org.junit.jupiter.api.Test
  public void testAdvancedClassloadingSampleString() throws Exception {
    UIMAClassLoader cl = new UIMAClassLoader(this.testClassPath, this.getClass().getClassLoader());
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
    URL[] urlClasspath = new URL[] { new File(this.testClassPath).toURL() };
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
