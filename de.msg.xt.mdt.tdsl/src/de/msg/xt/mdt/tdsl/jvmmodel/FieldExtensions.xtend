package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.Activity
import de.msg.xt.mdt.tdsl.tDsl.Field
import org.eclipse.emf.ecore.EObject
import de.msg.xt.mdt.tdsl.tDsl.Operation
import org.eclipse.xtext.naming.IQualifiedNameProvider
import javax.inject.Inject

class FieldExtensions {
	
	@Inject extension IQualifiedNameProvider
		
	def parentActivity(Field field) {
		var EObject current = field
		while (current != null && !(current instanceof Activity)) {
			current = current.eContainer
		}
		return current as Activity
	}
	
	def findOperationMappingForOperation(Field field, Operation operation) {
		for (op : field.operations) {
			if (op.operation == operation) {
				return op
			}
		}
		return null
	}
	
	def identifier(Field field) {
		if (field.uniqueId == null) field.fullyQualifiedName else field.uniqueId
	}
}