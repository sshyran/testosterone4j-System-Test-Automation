/*
* generated by Xtext
*/
package org.testosterone4j;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class TdslStandaloneSetup extends TdslStandaloneSetupGenerated{

	public static void doSetup() {
		new TdslStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

