/*
 * JungBok Cho
 * Triage System
 */
package triage;
import java.util.ArrayList;

/**
 * This is a program to build a hospital triage system implemented using a heap.
 * 
 * @author JungBok Cho
 * @version 1.0
 */
public class PatientPriorityQueue {
	
	private ArrayList<Patient> patients;   // heap property is always satisfied
	private int nextPatientNumber; 	       // num assigned to next added patient

	
	/**
	 * Creates an empty triage system with no patients.
	 */
	public PatientPriorityQueue() {
		this.patients = new ArrayList<Patient>();
		this.nextPatientNumber = 1;
	}
	

	/**
	 * Gets the list of patients currently in the waiting room
	 * 
	 * @return the list of patients that have not been called
	 */
	public ArrayList<Patient> getPatientList() {
		return patients;
	}

	
	/**
	 * Return a copy of patients arrayList
	 * 
	 * @return Return a copy of patients arrayList
	 */
	public ArrayList<Patient> getCopyList() {
		ArrayList<Patient> temp = new ArrayList<>();
		for (int i = 0; i < patients.size(); i++) {
			temp.add(patients.get(i));
		}
		return temp;
	}

	
	/**
	 * Add a Patient object to the heap
	 * 
	 * @param priorityCode The patient's priority code
	 * @param patientName  The patient's name
	 */
	public void addPatient(int priorityCode, String patientName) {
		Patient toAdd = new Patient(priorityCode, nextPatientNumber, patientName);
		patients.add(toAdd);
		percolateUp(patients.size() - 1); // Heapify
		nextPatientNumber++;
	}

	
	/**
	 * Return the object in the index of 0
	 * 
	 * @return Return the object in the root, Return null if the heap is empty
	 */
	public Patient peek() {
		if (patients.isEmpty()) {
			return null;
		} else {
			return patients.get(0);
		}
	}

	
	/**
	 * Remove an item from the heap
	 * 
	 * @return Return the minimum element in the heap 
	 *         Return null if the heap is empty
	 */
	public Patient dequeue() {
		if (patients.isEmpty()) {
			return null;
		} else {
			Patient root = patients.get(0);
			patients.set(0, patients.get(patients.size() - 1));
			patients.remove(patients.size() - 1);
			percolateDown(0); // Heapify
			return root;
		}
	}

	
	/**
	 * Change priorityCode of a Patient object
	 * 
	 * @param patientNum   The index of the target patient
	 * @param priorityCode The patient's priority code
	 * @param arrivalNum   The patient's name
	 */
	public void change(int patientNum, int priorityCode, int arrivalNum) {
		String patientName = patients.get(patientNum).getName();
		patients.set(patientNum, new Patient(priorityCode, arrivalNum, patientName));
		percolateUpForChange(patientNum);
		percolateDown(patientNum);
	}

	
	/**
	 * Return the size of heap
	 * 
	 * @return Return the size of heap
	 */
	public int size() {
		return patients.size();
	}
	

	/**
	 * Shifts up the element at patients[size() - 1]
	 * 
	 * @param index The elements to percolate up
	 */
	private void percolateUp(int index) {
		int parentIndex = (index - 1) / 2;
		Patient curr = patients.get(index);
		Patient parent = parent(index);

		// Check which one is lower, then, swap the values
		if (parentIndex >= 0 && curr.getPriorityCode() < parent.getPriorityCode()) {
			Patient temp = curr;
			patients.set(index, parent);
			patients.set(parentIndex, temp);
			percolateUp(parentIndex);
		// If the priorities are same, check the arrival orders
		} else if (parentIndex >= 0 && curr.getPriorityCode() == parent.getPriorityCode()) {
			if (curr.getArrivalOrder() < parent.getArrivalOrder()) {
				Patient temp = curr;
				patients.set(index, parent);
				patients.set(parentIndex, temp);
				percolateUp(parentIndex);
			}
		}
	}
	

	/**
	 * Shifts up the element at patients[size() - 1] Also, swap the values even
	 * though current priority code is equal to parent's priority code
	 * 
	 * @param index The elements to percolate up
	 */
	private void percolateUpForChange(int index) {
		int parentIndex = (index - 1) / 2;
		Patient curr = patients.get(index);
		Patient parent = parent(index);

		// Check which one is lower, then, swap the values
		if (parentIndex >= 0 && curr.getPriorityCode() < parent.getPriorityCode()) {
			Patient temp = curr;
			patients.set(index, parent);
			patients.set(parentIndex, temp);
			percolateUp(parentIndex);
		// If the priorities are same, check the arrival orders
		} else if (parentIndex >= 0 && curr.getPriorityCode() == parent.getPriorityCode()) {
			if (curr.getArrivalOrder() < parent.getArrivalOrder()) {
				Patient temp = curr;
				patients.set(index, parent);
				patients.set(parentIndex, temp);
				percolateUp(parentIndex);
			}
		}
	}
	

	/**
	 * Shifts up the element at patients[0]
	 * 
	 * @param index The elements to percolate down
	 */
	private void percolateDown(int index) {
		if (index < patients.size()) {
			int minIndex = index;

			// Check which priority is lower
			if (hasLeft(index) && patients.get(minIndex).getPriorityCode() 
								   > left(index).getPriorityCode()) {
				minIndex = index * 2 + 1;
			// If the priorities are same, check the arrival orders
			} else if (hasLeft(index) && patients.get(minIndex).getPriorityCode() 
									     == left(index).getPriorityCode()) {
				if (patients.get(minIndex).getArrivalOrder() > left(index).getArrivalOrder()) {
					minIndex = index * 2 + 1;
				}
			}
			
			// Check which priority is lower
			if (hasRight(index) && patients.get(minIndex).getPriorityCode() 
								    > right(index).getPriorityCode()) {
				minIndex = index * 2 + 2;
			// If the priorities are same, check the arrival orders
			} else if (hasRight(index) && patients.get(minIndex).getPriorityCode() 
									      == right(index).getPriorityCode()) {
				if (patients.get(minIndex).getArrivalOrder() > right(index).getArrivalOrder()) {
					minIndex = index * 2 + 2;
				}
			}

			// Swap the values
			if (minIndex != index) {
				Patient temp = patients.get(minIndex);
				patients.set(minIndex, patients.get(index));
				patients.set(index, temp);
				percolateDown(minIndex);
			}
		}
	}
	

	/**
	 * Check if the element has a left child
	 * 
	 * @param parentIndex The index of parent
	 * @return Return true if it has left child, otherwise false
	 */
	private boolean hasLeft(int parentIndex) {
		return 2 * parentIndex + 1 < patients.size();
	}

	
	/**
	 * Check if the element has a right child
	 * 
	 * @param parentIndex The index of parent
	 * @return Return true if it has right child, otherwise false
	 */
	private boolean hasRight(int parentIndex) {
		return 2 * parentIndex + 2 < patients.size();
	}
	

	/**
	 * Return the left child
	 * 
	 * @param parentIndex The index of parent
	 * @return Return the left child
	 */
	private Patient left(int parentIndex) {
		return patients.get(parentIndex * 2 + 1);
	}

	
	/**
	 * Return the right child
	 * 
	 * @param parentIndex The index of parent
	 * @return Return the right child
	 */
	private Patient right(int parentIndex) {
		return patients.get(parentIndex * 2 + 2);
	}

	
	/**
	 * Return the parent
	 * 
	 * @param childIndex The index of child
	 * @return Return the parent
	 */
	private Patient parent(int childIndex) {
		return patients.get((childIndex - 1) / 2);
	}

}
