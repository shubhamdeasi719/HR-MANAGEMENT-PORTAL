function updateDesignations() {
    const department = document.getElementById("department").value;
    const designation = document.getElementById("designation");

    // Clear previous options
    designation.innerHTML = "";

    // Add default option at the top
    const defaultOption = document.createElement("option");
    defaultOption.text = "Select Designation";
    defaultOption.value = ""; // Empty value for "Select Designation"
    designation.appendChild(defaultOption);

    let options = [];

    // Define designation options based on department
    if (department === 'development') {
        options = ['Software Engineer', 'Senior Developer', 'Team Lead'];
    } else if (department === 'qa') {
        options = ['QA Engineer', 'Automation Engineer', 'Test Lead'];
    } else if (department === 'networking') {
        options = ['Network Engineer', 'System Administrator'];
    } else if (department === 'hr') {
        options = ['HR Manager', 'HR Executive', 'Recruiter'];
    } else if (department === 'marketing') {
        options = ['Sales Executive', 'Marketing Manager'];
    } else if (department === 'security') {
        options = ['Security Officer', 'Security Analyst'];
    }

    // Add the new options to the designation dropdown
    options.forEach(function(designationValue) {
        const option = document.createElement("option");
        option.value = designationValue;
        option.text = designationValue;
        designation.appendChild(option);
    });

    // Ensure the previously selected designation is re-selected
    const selectedDesignation = document.getElementById("designation").value;
    if (selectedDesignation) {
        Array.from(designation.options).forEach(function(option) {
            if (option.value === selectedDesignation) {
                option.selected = true;
            }
        });
    }
	
	document.addEventListener('DOMContentLoaded', function () {
	    // Your code here
	    updateDesignations();
	});
}

// Initialize the designations when the page loads (in case of form re-rendering)
window.onload = function() {
    updateDesignations();
};


function updateRecord(employeeId)
{
	window.location.href=`/admin/update-employee?employeeId=${employeeId}`;
}


function deleteRecord(employeeId) {
    // Show SweetAlert2 confirmation dialog
    Swal.fire({
        title: 'Are you sure?',
        text: 'You won\'t be able to revert this!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        // If the user confirms, proceed with deletion
        if (result.isConfirmed) {
            window.location.href = `/admin/delete-employee?employeeId=${employeeId}`;
        }
    });
}

function adminAction(id, type) {
	// Show SweetAlert2 confirmation dialog
	   Swal.fire({
	       title: 'Are you sure?',
	       text: 'Do you want to '+type+" ?",
	       icon: 'warning',
	       showCancelButton: true,
	       confirmButtonColor: '#3085d6',
	       cancelButtonColor: '#d33',
	       confirmButtonText: 'Yes, '+type+" it!",
	       cancelButtonText: 'Cancel'
	   }).then((result) => {
	       // If the user confirms, proceed with deletion
	       if (result.isConfirmed) {
	           window.location.href = `/admin/admin-action?id=${id}&type=${type}`;
	       }
	   });
}





