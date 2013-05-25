package org.gegma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gegma.tasks.HumanTask;
import org.gegma.tasks.Task;

/**
 * 
 * @author rezo
 */
public class General {

    private static Map<String, List<ForUser>> userTasks = new HashMap<String, List<ForUser>>();
    private static Map<String, List<ForUser>> groupTasks = new HashMap<String, List<ForUser>>();

    public static void main(String[] args) throws Exception {

	// Create process and add event listener
	GegmaProcess proc = new GegmaProcess();
	proc.setListener(new ProcessListenerImpl());

	// Create all elements for flow.
	Place start = new Place();
	start.setName("Start");

	Task started = new Task();

	Place beforeValidate = new Place();
	beforeValidate.setName("before-validate");

	// Validates if start is correct
	Task validate = new Task(new SimpleAction(proc, "valid", Boolean.TRUE));
	validate.setName("validate");

	// place where goes valid tokens
	Place validPlace = new Place();
	validPlace.setName("valid_place");

	// place where goes invalid tokens
	Place invalidPlace = new Place();
	invalidPlace.setName("invalid_place");

	HumanTask calmAgain = new HumanTask(new SimpleAction(proc, "valid",
		Boolean.TRUE));
	calmAgain.setOwnerUser("simple-user");
	calmAgain.setDescription("new calm for trip");

	// Warns user that token is invalid
	Transition warnUser = new Task(new SimpleAction(proc, "valid",
		Boolean.TRUE));

	Place warnedPlace = new Place();
	warnedPlace.setName("User_Warned");

	// Create document task
	Transition createDocument = new Task(new SimpleAction(proc,
		"document_created", Boolean.TRUE));

	Place documentCreated = new Place();
	documentCreated.setName("document_created");

	// Transition to check if user goes abroad
	Transition checkOnAbroad = new Task(new SimpleAction(proc, "abroad",
		Boolean.TRUE));

	Place abroadChecked = new Place();
	abroadChecked.setName("abroad_checked");

	abroadChecked.addInput(checkOnAbroad);

	Transition addAddressats = new Task();
	Transition validateLocal = new Task();

	Place vmAddresatAdded = new Place();
	vmAddresatAdded.setName("vm_addresat_added");

	Place ciAddresatAdded = new Place();
	vmAddresatAdded.setName("ci_addresat_added");

	HumanTask signByCi = new HumanTask(new SimpleAction(proc,
		"trip_signed", Boolean.TRUE));
	signByCi.setOwnerGroup("ci-dep");
	signByCi.setDescription("dear ci, pleas let this user to has thip abroad");

	HumanTask signByVm = new HumanTask(new SimpleAction(proc,
		"trip_signed", Boolean.TRUE));
	signByVm.setOwnerUser("vm-person");
	signByVm.setDescription("dear vm, pleas let this user to has thip abroad");

	Place signedByCi = new Place();
	signedByCi.setName("sighned-by-ci");

	Place signedByVm = new Place();
	signedByVm.setName("sighned-by-vm");

	Transition signForAbroad = new Task();

	Place beforeFinance = new Place();
	beforeFinance.setName("before-finance");

	Transition desideFinancing = new Task(new SimpleAction(proc,
		"central-finance", Boolean.TRUE));

	Place financingDisided = new Place();
	financingDisided.setName("financing-desided");

	Transition notApprove = new Task();
	Place notApproved = new Place();
	notApproved.setName("not-approved");

	HumanTask signByCentral = new HumanTask(new SimpleAction(proc,
		"finance-sighned", Boolean.TRUE));
	signByCentral.setOwnerGroup("central-dep");
	signByCentral
		.setDescription("dear central finance department, pleas finance this user's abroad trip");
	HumanTask signByLocal = new HumanTask(new SimpleAction(proc,
		"finance-sighned", Boolean.TRUE));
	signByLocal.setOwnerGroup("local-dep");
	signByLocal
		.setDescription("dear local finance department, pleas finance this user's abroad trip");

	Place signedFinance = new Place();
	signedFinance.setName("sighned-by-finance");

	Transition finance = new HumanTask();
	Transition notFinance = new HumanTask();

	Place financed = new Place();
	financed.setName("financed");

	Place notFinanced = new Place();
	notFinanced.setName("not-financed");

	Transition notPermit = new Task();
	// Transition permit = new HumanTask();

	Place beforeFinish = new Place();
	beforeFinish.setName("before-finish");

	Transition finnish = new Task();

	Place end = new Place();
	end.setName("End");

	// Create arcs
	start.addOutput(started);

	beforeValidate.addInput(started);
	beforeValidate.addInput(calmAgain);

	beforeValidate.addOutput(validate);

	validPlace.addInput(validate);
	validPlace.addOutput(new Connection(createDocument, new Condition() {

	    @Override
	    public boolean isValid(GegmaProcess process) {

		Object validator = process.getParameters().get("valid");
		boolean valid = validator != null
			&& validator.equals(Boolean.TRUE);

		return valid;
	    }
	}));
	validPlace.addOutput(warnUser);

	invalidPlace.addInput(warnUser);
	invalidPlace.addOutput(calmAgain);

	documentCreated.addInput(createDocument);
	documentCreated.addOutput(checkOnAbroad);

	abroadChecked.addInput(checkOnAbroad);
	abroadChecked.addOutput(new Connection(addAddressats, new Condition() {

	    @Override
	    public boolean isValid(GegmaProcess process) {

		Object validator = process.getParameters().get("abroad");
		boolean valid = validator != null
			&& validator.equals(Boolean.TRUE);

		return valid;
	    }
	}));
	abroadChecked.addOutput(validateLocal);

	// Goes signing tree for abroad trip approval
	vmAddresatAdded.addInput(addAddressats);
	ciAddresatAdded.addInput(addAddressats);

	vmAddresatAdded.addOutput(signByVm);
	ciAddresatAdded.addOutput(signByCi);

	signedByVm.addInput(signByVm);
	signedByVm.addOutput(signForAbroad);

	signedByCi.addInput(signByCi);
	signedByCi.addOutput(signForAbroad);

	// Converging nodes
	beforeFinance.addInput(signForAbroad);
	beforeFinance.addInput(validateLocal);

	beforeFinance.addOutput(new Connection(desideFinancing,
		new Condition() {

		    @Override
		    public boolean isValid(GegmaProcess process) {

			Object validator = process.getParameters().get(
				"trip_signed");
			boolean valid = validator != null
				&& validator.equals(Boolean.TRUE);

			return valid;
		    }
		}));
	beforeFinance.addOutput(notApprove);

	// Branch if not approved
	notApproved.addInput(notApprove);

	financingDisided.addInput(desideFinancing);

	financingDisided.addOutput(new Connection(signByCentral,
		new Condition() {

		    @Override
		    public boolean isValid(GegmaProcess process) {

			Object validator = process.getParameters().get(
				"central-finance");
			boolean valid = validator != null
				&& validator.equals(Boolean.TRUE);

			return valid;
		    }
		}));
	financingDisided.addOutput(new Connection(signByLocal, new Condition() {

	    @Override
	    public boolean isValid(GegmaProcess process) {

		Object validator = process.getParameters().get("local-finance");
		boolean valid = validator != null
			&& validator.equals(Boolean.TRUE);

		return valid;
	    }
	}));

	signedFinance.addInput(signByCentral);
	signedFinance.addInput(signByLocal);
	signedFinance.addOutput(new Connection(finance, new Condition() {

	    @Override
	    public boolean isValid(GegmaProcess process) {

		Object validator = process.getParameters().get(
			"finance-sighned");
		boolean valid = validator != null
			&& validator.equals(Boolean.TRUE);

		return valid;
	    }
	}));
	signedFinance.addOutput(notFinance);

	notApproved.addInput(notFinance);

	notApproved.addOutput(notPermit);

	beforeFinish.addInput(notPermit);
	beforeFinish.addInput(finance);

	beforeFinish.addOutput(finnish);

	end.addInput(finnish);

	// Set process parameters.
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("employee", "Rezo");
	params.put("abroad", true);
	params.put("own", false);

	proc.setParameters(params);
	// Let's start!
	proc.startProcess(start);

	// Print collected group tasks and wait 1 second for effective
	// presentaton.
	printGroupTasks();
	Thread.sleep(1000);

	// User completes his element.
	ForUser ciTask = groupTasks.get("ci-dep").remove(0);
	System.out.println(ciTask.getDescription());
	ForUser vmTask = userTasks.get("vm-person").remove(0);
	System.out.println(vmTask.getDescription());
	proc.doNext();

	// Next pause, where another user decision takes place.
	printGroupTasks();
	Thread.sleep(1000);

	// Next user completes element.
	ForUser centralTask = groupTasks.get("central-dep").remove(0);
	System.out.println(centralTask.getDescription());
	proc.doNext();
    }

    private static void printGroupTasks() {

	for (List<ForUser> ful : groupTasks.values()) {
	    for (ForUser fu : ful) {
		System.out.format("%s\t%s\n", fu.getOwnerGroup(),
			fu.getDescription());
	    }
	}
    }

    private static void addCreateMapElement(Map<String, List<ForUser>> map,
	    String key, ForUser value) {
	List<ForUser> fu = userTasks.get(key);
	if (fu == null) {
	    fu = new ArrayList<ForUser>();
	    map.put(key, fu);
	}
	fu.add(value);
    }

    private static class ProcessListenerImpl implements ProcessListener {

	@Override
	public void beforePlace(GegmaProcess process, Place place) {
	    System.out.println(place.getName());
	}

	@Override
	public void execute(GegmaProcess process, Place place)
		throws IOException {

	    Transition transition = place.getPath();

	    if (transition instanceof HumanTask) {
		// Fill human task db
		HumanTask task = (HumanTask) transition;
		ForUser forUser = new ForUser(process.getProcessId(),
			place.getPlaceId(), task.getOwnerUser(),
			task.getOwnerGroup(), task.getDescription());
		if (task.getOwnerUser() != null) {
		    addCreateMapElement(userTasks, task.getOwnerUser(), forUser);
		}
		if (task.getOwnerGroup() != null) {
		    addCreateMapElement(groupTasks, task.getOwnerGroup(),
			    forUser);
		}
	    }
	}

	@Override
	public void afterPlace(GegmaProcess process, Place place) {
	}
    }
}