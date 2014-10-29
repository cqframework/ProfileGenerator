package org.socraticgrid.quick.fhir.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.Profile;
import org.hl7.fhir.instance.model.Profile.ConstraintComponent;
import org.hl7.fhir.instance.model.Profile.ElementComponent;
import org.hl7.fhir.instance.model.Profile.ElementDefinitionComponent;
import org.hl7.fhir.instance.model.Profile.ElementDefinitionMappingComponent;
import org.hl7.fhir.instance.model.Profile.ProfileExtensionDefnComponent;
import org.hl7.fhir.instance.model.Profile.ProfileStructureComponent;
import org.hl7.fhir.instance.model.Profile.TypeRefComponent;
import org.junit.Before;
import org.junit.Test;
import org.socraticgrid.eap.xmi.reader.UmlModelLoader;
import org.socraticgrid.uml.UmlClass;
import org.socraticgrid.uml.UmlModel;

/**
 * Test for the generation of the ConditionOccurrence FHIR profile.
 * Please keep underlying QUICK.xmi file  and test current to reflect model changes.
 * 
 * @author cnanjo
 *
 */
public class ProcedureProposalTest extends TestBase {
	
	@Test
	public void testGenerateConditionOccurrenceProfile() {
		UmlClass conditionOccurrence = (UmlClass)model.getObjectByName("ProcedureProposalOccurrence");
		Profile profile = profileGenerator.generateSimpleProfile(conditionOccurrence, new UmlClass("Other"));
		System.out.println(FhirProfileGenerator.generateProfileAsXml(profile));
	}
}
