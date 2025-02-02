/*
 * Copyright 2009 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.truth.Truth.assertThat;

import com.google.javascript.rhino.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for PrepareAst.
 *
 */
@RunWith(JUnit4.class)
public final class PrepareAstTest extends CompilerTestCase {
  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return null; // unused
  }

  @Test
  public void testFreeCall1() {
    Node root = parseExpectedJs("foo();");
    Node script = root.getFirstChild();
    checkState(script.isScript());
    Node firstExpr = script.getFirstChild();
    Node call = firstExpr.getFirstChild();
    checkState(call.isCall());

    assertThat(call.getBooleanProp(Node.FREE_CALL)).isTrue();
  }

  @Test
  public void testFreeCall2() {
    Node root = parseExpectedJs("x.foo();");
    Node script = root.getFirstChild();
    checkState(script.isScript());
    Node firstExpr = script.getFirstChild();
    Node call = firstExpr.getFirstChild();
    checkState(call.isCall());

    assertThat(call.getBooleanProp(Node.FREE_CALL)).isFalse();
  }

  @Test
  public void testTaggedTemplateFreeCall1() {
    Node root = parseExpectedJs("foo``;");
    Node script = root.getFirstChild();
    checkState(script.isScript());
    Node firstExpr = script.getFirstChild();
    Node call = firstExpr.getFirstChild();
    checkState(call.isTaggedTemplateLit());

    assertThat(call.getBooleanProp(Node.FREE_CALL)).isTrue();
  }

  @Test
  public void testTaggedTemplateFreeCall2() {
    Node root = parseExpectedJs("x.foo``;");
    Node script = root.getFirstChild();
    checkState(script.isScript());
    Node firstExpr = script.getFirstChild();
    Node call = firstExpr.getFirstChild();
    checkState(call.isTaggedTemplateLit());

    assertThat(call.getBooleanProp(Node.FREE_CALL)).isFalse();
  }

  @Test
  public void optionalFreeCall1() {
    Node root = parseExpectedJs("foo?.();");
    Node script = root.getFirstChild();
    checkState(script.isScript());
    Node firstExpr = script.getFirstChild();
    Node call = firstExpr.getFirstChild();
    checkState(call.isOptChainCall());

    assertThat(call.getBooleanProp(Node.FREE_CALL)).isTrue();
  }

  @Test
  public void optChainFreeCall() {
    Node root = parseExpectedJs("x?.foo();");
    Node script = root.getFirstChild();
    checkState(script.isScript());
    Node firstExpr = script.getFirstChild();
    Node call = firstExpr.getFirstChild();
    checkState(call.isOptChainCall());

    assertThat(call.getBooleanProp(Node.FREE_CALL)).isFalse();
  }
}

