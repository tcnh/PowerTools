/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powerTools.engine.sources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.TestLine;
import org.powerTools.engine.symbol.Scope;


// A Procedure is a scripted instruction.
public final class Procedure {
	public Procedure (String name) {
		mName 			= name;
		mParameters		= new ArrayList<Parameter> ();
		mInstructions 	= new ArrayList<List<String>> ();
	}

	
	public String getName () {
		return mName;
	}
	
	
	public void addParameter (String name, boolean isOutput) {
		for (Parameter parameter : mParameters) {
			if (parameter.mName.equalsIgnoreCase (name)) {
				throw new ExecutionException (String.format ("duplicate parameter name '%s'", parameter.mName));
			}
		}

		mParameters.add (new Parameter (name, isOutput));
	}
	
	public void addInstruction (List<String> instruction) {
		mInstructions.add (instruction);
	}
	
	void createParameters (Scope scope, TestLine testLine) {
		checkNrOfArguments (testLine.getNrOfParts () - 1);

		int partNr = 0;
		for (Parameter parameter : mParameters) {
			final String argument = testLine.getPart (++partNr);
			if (parameter.mIsOutput) {
				scope.createParameter (parameter.mName, argument);
			} else {
				scope.createConstant (parameter.mName, argument);
			}
		}
	}

	public Iterator<List<String>> instructionIterator () {
		return mInstructions.iterator ();
	}


	// private members
	private class Parameter {
		public final String mName;
		public final boolean mIsOutput;
		
		public Parameter (String name, boolean isOutput) {
			mName		= name;
			mIsOutput	= isOutput;
		}
	}
	
	private final String mName;
	private final List<Parameter> mParameters;
	private final List<List<String>> mInstructions;
	
	private void checkNrOfArguments (int nrOfArguments) {
		final int nrOfParameters = mParameters.size ();
		if (nrOfArguments != nrOfParameters) {
			throw new ExecutionException (String.format ("procedure %s expects %d arguments but receives %d", mName, nrOfParameters, nrOfArguments));
		}
	}
}