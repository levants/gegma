package org.gegma;

/**
 * 
 * @author rezo
 */
public class ForUser {

    private Long processId;

    private Long elementId;

    private String ownerUser;

    private String ownerGroup;

    private String description;

    public ForUser(Long processId, Long elementId, String ownerUser,
	    String ownerGroup, String description) {

	this.processId = processId;

	this.elementId = elementId;

	this.ownerUser = ownerUser;

	this.ownerGroup = ownerGroup;

	this.description = description;
    }

    public Long getProcessId() {
	return processId;
    }

    public void setProcessId(Long processId) {
	this.processId = processId;
    }

    public Long getElementId() {
	return elementId;
    }

    public void setElementId(Long elementId) {
	this.elementId = elementId;
    }

    public String getOwnerUser() {
	return ownerUser;
    }

    public void setOwnerUser(String ownerUser) {
	this.ownerUser = ownerUser;
    }

    public String getOwnerGroup() {
	return ownerGroup;
    }

    public void setOwnerGroup(String ownerGroup) {
	this.ownerGroup = ownerGroup;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

}
