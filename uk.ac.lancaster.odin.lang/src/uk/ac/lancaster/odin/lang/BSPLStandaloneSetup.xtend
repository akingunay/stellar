/*
 * generated by Xtext 2.10.0
 */
package uk.ac.lancaster.odin.lang


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class BSPLStandaloneSetup extends BSPLStandaloneSetupGenerated {

	def static void doSetup() {
		new BSPLStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
