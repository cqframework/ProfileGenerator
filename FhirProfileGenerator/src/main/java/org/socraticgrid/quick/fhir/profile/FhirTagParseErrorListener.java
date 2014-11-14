package org.socraticgrid.quick.fhir.profile;

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Enhance this later. For now, record error count.
 * 
 * @author cnanjo
 *
 */
public class FhirTagParseErrorListener  extends BaseErrorListener {
	
	private int parseErrorCount = 0;

	public FhirTagParseErrorListener() {}
	
	@Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e)
    {
    	List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        System.err.println("rule stack: "+stack);//Log rather than sysout
        System.err.println("line "+line+":"+charPositionInLine+" at "+
                           offendingSymbol+": "+msg);
        parseErrorCount++;
    }

	public int getParseErrorCount() {
		return parseErrorCount;
	}
	
	public boolean hasErrors() {
		return getParseErrorCount() > 0;
	}
}

