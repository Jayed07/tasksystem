## H2 Simple Spring Boot Application for task management
An application allowing easy management of employees and projects, which finds the pair of employees who worked the longest on joint projects and calculates the time of their collaboration.

### H3 Data Import
Importing data into the system can be done in three ways:
1. Adding an employee - adding an employee is done through a simplified form, only the employee's ID needs to be filled in, the field cannot be empty and must be a positive number;
2. Adding a project - adding a project is done using a simplified form, only needing to fill in the project's ID, the field cannot be empty and must be a positive number;
3. Uploading a CSV file - in order to add multiple data at once, it is necessary to upload a CSV file, with lines in the following format "EmployeeID, ProjectID, DateFrom, DateTo". Each row creates a Project Card that contains an Employee ID, a Project ID, a start date and an end date, and if the value of the end date is NULL, it is considered to be a project that is still being worked on by the specific employee, in the present. Note that if any field is filled incorrectly it will result in invalid data, which means that a Project Card will not be created.

### H3 Dashboard
In the Dashboard, you can find three subpages:
1. Projects - on the Projects page, all projects added to the database are visualized, first the active projects (those on which employees are working) will be visualized, and then the inactive ones (those on which no employees are working);
2. Employees - the Employees page will display all the employees added to the database, first displaying the occupied employees (those who have projects, regardless of whether they are being worked on to today's date), then the free employees will be displayed (those without projects);
3. Statistics - on the Statistics page, the pair of employees who worked on joint projects, simultaneously, for the longest time of all employees is visualized, and their total time (in days) for all projects, as is the time (in days) for each project they worked on together.

### H3 Editing and Deleting Data
To edit Employee or Project details, simply click the VIEW button located at the bottom of each Employee/Project card section. It will lead you to the details page where you will see Edit and Delete buttons.
1. Delete - to delete an Employee/Project, simply press the DELETE button. When deleting an Employee or Project, all Project Cards associated with them will also be deleted;
2. Edit - to edit an Employee/Project, press the EDIT button, which leads to a form containing the Employee/Project ID field which cannot be edited, and below that, a drop-down menu from which you can select a Project/Employee. When adding an Employee-Project relationship, a new Project Card is created, with a start date of today's date and an end date left blank.
