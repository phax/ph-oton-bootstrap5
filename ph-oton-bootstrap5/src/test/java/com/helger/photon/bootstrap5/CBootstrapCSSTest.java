/*
 * Copyright (C) 2025-2026 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.photon.bootstrap5;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsHashSet;
import com.helger.collection.commons.ICommonsSet;
import com.helger.html.css.ICSSClassProvider;

/**
 * Test class for class {@link CBootstrapCSS}.
 *
 * @author Philip Helger
 */
public final class CBootstrapCSSTest
{
  @Test
  public void testAllCSSClassesAreUniqueAndValid ()
  {
    final ICommonsSet <String> aUsedCSSClasses = new CommonsHashSet <> ();
    int nCount = 0;
    for (final Field aField : CBootstrapCSS.class.getDeclaredFields ())
      if (Modifier.isPublic (aField.getModifiers ()) &&
          Modifier.isStatic (aField.getModifiers ()) &&
          ICSSClassProvider.class.isAssignableFrom (aField.getType ()))
      {
        final ICSSClassProvider aCSSClassProvider;
        try
        {
          aCSSClassProvider = (ICSSClassProvider) aField.get (null);
        }
        catch (final IllegalAccessException ex)
        {
          throw new IllegalStateException ("Failed to read field " + aField.getName (), ex);
        }
        assertNotNull (aField.getName (), aCSSClassProvider);

        final String sCSSClass = aCSSClassProvider.getCSSClass ();
        assertTrue (aField.getName (), StringHelper.isNotEmpty (sCSSClass));
        assertFalse (aField.getName () + " contains a space: '" + sCSSClass + "'", sCSSClass.contains (" "));
        assertTrue (aField.getName () + " is a duplicate of '" + sCSSClass + "'", aUsedCSSClasses.add (sCSSClass));
        nCount++;
      }
    assertTrue ("Found only " + nCount + " CSS classes", nCount > 1000);
  }
}
