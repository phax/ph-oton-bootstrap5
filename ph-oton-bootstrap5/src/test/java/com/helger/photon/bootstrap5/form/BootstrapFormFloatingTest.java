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
package com.helger.photon.bootstrap5.form;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.html.hc.html.forms.HCEdit;
import com.helger.html.hc.html.forms.HCTextArea;

/**
 * Test class for class {@link BootstrapFormFloating}.
 *
 * @author Philip Helger
 */
public final class BootstrapFormFloatingTest
{
  @Test
  public void testEdit ()
  {
    final HCEdit aEdit = new HCEdit ("field");
    final String sHTML = getAsHTMLString (new BootstrapFormFloating (aEdit, "Label"));
    // The control must come first, the label afterwards
    assertEquals ("<div class=\"form-floating\">" +
                  "<input id=\"" +
                  aEdit.getID () +
                  "\" class=\"form-control\" name=\"field\" type=\"text\" placeholder=\"Label\" />" +
                  "<label for=\"" +
                  aEdit.getID () +
                  "\">Label</label>" +
                  "</div>",
                  sHTML);
  }

  @Test
  public void testTextArea ()
  {
    final HCTextArea aTextArea = new HCTextArea ("ta");
    final String sHTML = getAsHTMLString (new BootstrapFormFloating (aTextArea, "Comment"));
    assertTrue (sHTML, sHTML.contains ("class=\"form-floating\""));
    assertTrue (sHTML, sHTML.contains ("placeholder=\"Comment\""));
    assertTrue (sHTML, sHTML.contains ("<label for=\"" + aTextArea.getID () + "\">Comment</label>"));
  }

  @Test
  public void testExistingPlaceholderIsKept ()
  {
    final HCEdit aEdit = new HCEdit ("field").setPlaceholder ("Own");
    final String sHTML = getAsHTMLString (new BootstrapFormFloating (aEdit, "Label"));
    assertTrue (sHTML, sHTML.contains ("placeholder=\"Own\""));
  }

  @Test
  public void testWithoutLabel ()
  {
    final String sHTML = getAsHTMLString (new BootstrapFormFloating (new HCEdit ("field"), null));
    assertTrue (sHTML, sHTML.contains ("class=\"form-floating\""));
    assertTrue (sHTML, !sHTML.contains ("<label"));
  }

  @Test
  public void testFeedback ()
  {
    assertEquals ("<div class=\"valid-feedback\">ok</div>",
                  getAsHTMLString (new BootstrapValidFeedback ().addChild ("ok")));
    assertEquals ("<div class=\"invalid-feedback\">nope</div>",
                  getAsHTMLString (new BootstrapInvalidFeedback ().addChild ("nope")));
  }
}
