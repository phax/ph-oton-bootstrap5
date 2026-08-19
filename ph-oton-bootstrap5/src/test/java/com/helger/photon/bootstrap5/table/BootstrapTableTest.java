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
package com.helger.photon.bootstrap5.table;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapTable}.
 *
 * @author Philip Helger
 */
public final class BootstrapTableTest
{
  @Test
  public void testStyles ()
  {
    final BootstrapTable aTable = new BootstrapTable ();
    aTable.addBodyRow ().addCells ("a", "b");
    aTable.setStriped (true).setBordered (true).setHover (true).setCondensed (true);
    final String sHTML = getAsHTMLString (aTable);
    assertTrue (sHTML, sHTML.contains ("table-striped"));
    assertTrue (sHTML, sHTML.contains ("table-bordered"));
    assertTrue (sHTML, sHTML.contains ("table-hover"));
    // Bootstrap 5 uses "table-sm" instead of the Bootstrap 3 "table-condensed"
    assertTrue (sHTML, sHTML.contains ("table-sm"));
  }

  @Test
  public void testRemoveStyles ()
  {
    final BootstrapTable aTable = new BootstrapTable ();
    aTable.addBodyRow ().addCell ("a");
    aTable.setStriped (true).setStriped (false);
    final String sHTML = getAsHTMLString (aTable);
    assertTrue (sHTML, !sHTML.contains ("table-striped"));
  }
}
