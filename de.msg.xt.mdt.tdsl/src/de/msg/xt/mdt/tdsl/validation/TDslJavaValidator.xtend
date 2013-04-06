package de.msg.xt.mdt.tdsl.validation

import org.eclipse.xtext.validation.Check
import de.msg.xt.mdt.tdsl.tDsl.Field
import javax.inject.Inject
import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions
import de.msg.xt.mdt.tdsl.tDsl.TDslPackage
import org.eclipse.xtext.xbase.XExpression
import de.msg.xt.mdt.tdsl.tDsl.ActivityOperation

class TDslJavaValidator extends AbstractTDslJavaValidator {
	
	@Inject extension MetaModelExtensions
	
	@Check
	def checkOperationMapping(Field field) {
		val operationMappings = field.getOperations();
		val operations = field.getControl().getOperations();
		if (operationMappings.size != field.control.operations.size()) {
			for (op : operations) {
				var found = false;
				for (opMapping : operationMappings) {
					if (opMapping.getName().equals(op)) {
						found = true;
					}
				}
				if (!found) {
					error("An operation mapping must be defined for operation " + op.getName(),
							TDslPackage$Literals::FIELD__OPERATIONS);
				}
			}
		}
	}
	
	
	@Check
	def checkControlsInToolkit(Field field) {
		if(!field.parentActivity.toolkit.controls.contains(field.control)) {
			error ("The control '" + field.control.name + "' is not included in the current toolkit!", TDslPackage$Literals::FIELD__CONTROL)
		}
	}
	
	@Check
	override checkImplicitReturn(XExpression expr) {
		if (expr.eContainer() instanceof ActivityOperation) {
			return;
		}
		super.checkImplicitReturn(expr);
	}
}