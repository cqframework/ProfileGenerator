package org.socraticgrid.quick.fhir.profile;

import org.hl7.fhir.instance.model.Profile;
import org.junit.Test;
import org.socraticgrid.uml.UmlClass;

/**
 * Test for the generation of the ConditionOccurrence FHIR profile.
 * Please keep underlying QUICK.xmi file  and test current to reflect model changes.
 * 
 * @author cnanjo
 *
 */
public class ProcedureProposalTest extends TestBase {
	
	@Test
	public void testProcedureProposal() {
		UmlClass conditionOccurrence = (UmlClass)model.getObjectByName("ProcedureProposalOccurrence");
		Profile profile = profileGenerator.generateSimpleProfile(conditionOccurrence, new UmlClass("Other"));
		System.out.println(FhirProfileGenerator.generateProfileAsXml(profile));
	}
}
