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

package org.powerTools.engine.core;

import org.powerTools.engine.Context;
import org.powerTools.engine.reports.ReportFactory;
import org.powerTools.engine.sources.TestSource;
import org.powerTools.engine.sources.XlsTestSource;
import org.powerTools.engine.symbol.Scope;


public class ExcelEngine extends Engine {
	public static void main (String[] args) {
		if (args.length != 2) {
			System.err.println ("Please specify a directory and a file");
		} else {
			new ExcelEngine (args[0]).run (args[1]);
		}
	}

	
	public ExcelEngine (String resultsDirectory) {
		this (new RunTimeImpl (new Context (resultsDirectory)));
	}

	public ExcelEngine (RunTimeImpl runTime) {
		super (runTime);

		if (!ReportFactory.createKeywordsHtmlLog (runTime.getContext ())) {
			mPublisher.publishError ("could not open HTML log");
		}

		BuiltinKeywords.register (runTime, mInstructions);
	}

	@Override
	public final void run (String fileName) {
		final TestSource source = XlsTestSource.createTestSource (fileName, Scope.getGlobalScope ());
		mRunTime.mSourceStack.initAndPush (source);
		run ();
		mPublisher.finish ();
	}
}