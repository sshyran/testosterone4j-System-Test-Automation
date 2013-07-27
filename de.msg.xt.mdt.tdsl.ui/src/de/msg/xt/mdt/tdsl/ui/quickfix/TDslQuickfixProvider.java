package de.msg.xt.mdt.tdsl.ui.quickfix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import de.msg.xt.mdt.tdsl.jvmmodel.MetaModelExtensions;
import de.msg.xt.mdt.tdsl.tDsl.Activity;
import de.msg.xt.mdt.tdsl.tDsl.ControlOperationParameter;
import de.msg.xt.mdt.tdsl.tDsl.DataTypeMapping;
import de.msg.xt.mdt.tdsl.tDsl.Field;
import de.msg.xt.mdt.tdsl.tDsl.Operation;
import de.msg.xt.mdt.tdsl.tDsl.OperationMapping;
import de.msg.xt.mdt.tdsl.tDsl.TDslFactory;
import de.msg.xt.mdt.tdsl.tDsl.Toolkit;
import de.msg.xt.mdt.tdsl.validation.TDslJavaValidator;

public class TDslQuickfixProvider extends DefaultQuickfixProvider {

	@Inject
	MetaModelExtensions metaModelExtensions;

	@Fix(TDslJavaValidator.UNSUFFICIENT_OPERATION_MAPPINGS)
	public void capitalizeName(final Issue issue,
			final IssueResolutionAcceptor acceptor) {
		acceptor.accept(
				issue,
				"Insert missing operation mappings",
				"Insert all necessary operation mappings using default datatypes.",
				"upcase.png", new ISemanticModification() {

					public void apply(final EObject element,
							final IModificationContext context)
							throws Exception {
						final Field field = (Field) element;
						final List<Operation> controlOperations = field
								.getControl().getOperations();
						final Map<Operation, OperationMapping> opMappings = new HashMap<Operation, OperationMapping>();
						for (final OperationMapping opMapping : field
								.getOperations()) {
							opMappings.put(opMapping.getName(), opMapping);
						}
						for (final Operation op : controlOperations) {
							if (!opMappings.containsKey(op)) {
								insertMappingForOperation(field, op);
							}
						}
					}

					private void insertMappingForOperation(final Field field,
							final Operation op) {
						final TDslFactory factory = TDslFactory.eINSTANCE;
						final OperationMapping mapping = factory
								.createOperationMapping();
						mapping.setName(op);
						mapping.setDataType(metaModelExtensions
								.defaultDataType(field, op.getReturnType()));
						final List<ControlOperationParameter> params = op
								.getParams();
						for (final ControlOperationParameter param : params) {
							final DataTypeMapping dtMapping = factory
									.createDataTypeMapping();
							dtMapping.setName(param);
							dtMapping.setDatatype(metaModelExtensions
									.defaultDataType(field, param.getType()));
							mapping.getDataTypeMappings().add(dtMapping);
						}
						field.getOperations().add(mapping);
					}
				});
	}

	@Fix(TDslJavaValidator.CONTROL_NOT_IN_TOOLKIT)
	public void addControlToToolkit(final Issue issue,
			final IssueResolutionAcceptor acceptor) {
		final String label = "Add control '" + issue.getData()[0]
				+ "' to toolkit";
		acceptor.accept(issue, label, label, "", new ISemanticModification() {

			public void apply(final EObject element,
					final IModificationContext context) throws Exception {
				final Field field = (Field) element;
				final Activity act = metaModelExtensions.parentActivity(field);
				final Toolkit toolkit = metaModelExtensions.getToolkit(act);
				if (toolkit != null) {
					toolkit.getControls().add(field.getControl());
				}
			}
		});
	}
}
