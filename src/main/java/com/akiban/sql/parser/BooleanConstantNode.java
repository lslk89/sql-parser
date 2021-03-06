/**
 * Copyright © 2012 Akiban Technologies, Inc.  All rights
 * reserved.
 *
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This program may also be available under different license terms.
 * For more information, see www.akiban.com or contact
 * licensing@akiban.com.
 *
 * Contributors:
 * Akiban Technologies, Inc.
 */

/* The original from which this derives bore the following: */

/*

   Derby - Class org.apache.derby.impl.sql.compile.BooleanConstantNode

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package com.akiban.sql.parser;

import com.akiban.sql.StandardException;
import com.akiban.sql.types.TypeId;

import java.sql.Types;

public class BooleanConstantNode extends ConstantNode
{
    private boolean booleanValue;
    private boolean unknownValue;

    /**
     * Initializer for a BooleanConstantNode.
     *
     * @param arg1 A boolean containing the value of the constant OR The TypeId for the type of the node
     *
     * @exception StandardException
     */
    public void init(Object arg1) throws StandardException {
        if (arg1 == null) {
            /* Fill in the type information in the parent ValueNode */
            super.init(TypeId.BOOLEAN_ID,
                       Boolean.TRUE,
                       4);

            setValue(null);
        }
        else if (arg1 instanceof Boolean) {
            booleanValue = ((Boolean)arg1).booleanValue();

            /* Fill in the type information in the parent ValueNode */
            super.init(TypeId.BOOLEAN_ID,
                       Boolean.FALSE,
                       booleanValue ? 4 : 5);

            super.setValue(arg1);
        }
        else {
            super.init(arg1,
                       Boolean.TRUE,
                       TypeId.BOOLEAN_MAXWIDTH);
            unknownValue = true;
        }
    }

    /**
     * Fill this node with a deep copy of the given node.
     */
    public void copyFrom(QueryTreeNode node) throws StandardException {
        super.copyFrom(node);

        BooleanConstantNode other = (BooleanConstantNode)node;
        this.booleanValue = other.booleanValue;
        this.unknownValue = other.unknownValue;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    /**
     * Return an Object representing the bind time value of this
     * expression tree.  If the expression tree does not evaluate to
     * a constant at bind time then we return null.
     * This is useful for bind time resolution of VTIs.
     * RESOLVE: What do we do for primitives?
     *
     * @return An Object representing the bind time value of this expression tree.
     *               (null if not a bind time constant.)
     *
     */
    Object getConstantValueAsObject() {
        return booleanValue ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Return the value as a string.
     *
     * @return The value as a string.
     *
     */
    String getValueAsString() {
        if (booleanValue) {
            return "true";
        }
        else {
            return "false";
        }
    }

    /**
     * Does this represent a true constant.
     *
     * @return Whether or not this node represents a true constant.
     */
    public boolean isBooleanTrue() {
        return (booleanValue && !unknownValue);
    }

    /**
     * Does this represent a false constant.
     *
     * @return Whether or not this node represents a false constant.
     */
    public boolean isBooleanFalse() {
        return (!booleanValue && !unknownValue);
    }

}
