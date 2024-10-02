# Grading Report (SCIPER 356004)

You will find below a breakdown of your points. At the bottom of this document, you will find a detailed view of your user stories feedback as well as your commit messages evaluation.

## Points Breakdown

| **Task**                                                 | **Points Awarded**                             | **Feedback**                                  |
| -------------------------------------------------------- | ---------------------------------------------- | --------------------------------------------- |
| **Initial Setup**                                        | 5 / 5                                  | ✅                                  |
| **CI Setup**                                             | 5 / 5                                     | ✅                                     |
| **Greeting Tests**                                       | **20 / 20**                    |                                               |
| &nbsp;&nbsp;&nbsp;everythingIsDisplayed                  | 6 / 6                  | ✅                  |
| &nbsp;&nbsp;&nbsp;displayHasCorrectDefaultValue          | 4 / 4          | ✅          |
| &nbsp;&nbsp;&nbsp;displayCorrectlyUpdates                | 10 / 10               | ✅                |
| **Sign-in & Login Tests**                                | **10** / 10                       |                                               |
| &nbsp;&nbsp;&nbsp;titleAndButtonAreCorrectlyDisplayed    | 4 / 4    | ✅    |
| &nbsp;&nbsp;&nbsp;googleSignInReturnsValidActivityResult | 6 / 6 | ✅ |
| **User stories and commit messages score**               | 4.833 / 10                             | [See Notes](#notes)                           |
| **Total score**                                          | 44.833 / 50                           |                                               |

---

## Final Score

- Congratulations, your code fulfilled all Bronze requirements, so it qualified for the Gold tier but not for Platinum (as explained in the [README](https://github.com/swent-epfl/bootcamp-f24-Shayan105/blob/main/docs/ToDoApp/README.md#grading-information)).
-   **Grade:** 5.483

---

## Notes

#### Grading Overview

The B1 grade is calculated as `1 + score/10`.

#### User stories and commit messages grading details:

-   **Gold Level**: `min(5, (STORIES_SCORE * 3/4) + (COMMITS_SCORE * 2/4))`
-   **Platinum Level**: `(STORIES_SCORE * 6/5) + (COMMITS_SCORE * 4/5)`

---

<details>
  <summary><strong>User Stories (Click to expand)</strong></summary>

| **User Stories**               | **Score [0-5]** | **Comments** |
| ------------------------------ | --------------- | ------------ |
| 1. As a new user, I want to add time alarm some to to-do activities so that I will no forget to do important part of the to-do list.                     | 4   | The user story has a good intention but it contains grammatical errors and awkward phrasing which detract from its clarity. Specifically, 'no forget' should be 'not forget', and 'some to' is unnecessary. Additionally, the feature could be better defined as 'set reminders for' instead of 'add time alarm' to make it clearer.   |
| 2. As a user, I want to create a hierarchy of activities so that I can easily see which activities should be done first.                     | 4   | This user story is well-structured and clearly conveys both the feature and the goal. It identifies a useful function for task prioritization, which is critical in task management applications. However, it could be improved by specifying what kind of hierarchy (e.g., due dates or importance) the user is looking for.   |
| **Average User Stories Score** | 4 / 5  |              |

</details>

---

<details>
  <summary><strong>Commit messages (Click to expand)</strong></summary>

| **Commit messages** | **Score [0-5]** | **Comments** |
| ------------------- | --------------- | ------------ |
| Modify the NavigationAction currentRout to handle null cases | 5 | The message describes what part of the code has been modified and for which reasons. |
| Modify ListToDo view Model to handle not logged users | 5 | This commit explains clearly the reasons for the change. |
| Modify ListToDo view Model to handle not logged users | 5 | This commit explains clearly the reasons for the change. However the formulation is a bit confusing. |
| Modify ListToDo view Model to handle not logged users | 5 | This commit explains clearly the reasons for the change. However the formulation is a bit confusing. |
| Ktfmt FOrmatted | 4 | The message does not start with imperative. Also, it is confusing since it implies that it was ktfmt that was formatted, while it is ktfmt that was used to format the code. try explaining more clearly what is the change that was done. |
| Fix ListToDosModelTest: Repository was accessed twice because of the init of the cass | 3 | The message is descriptive and explains the reasaons that led to the fix. However it is still a bit vague (confusion on the meaning of cass) and on what was done to fix the problem. Also, the subject line is too long (more than 50 characters). |
| B2 formatted | 2 | The message is too vague and does not provide enough context about what was done. It should explained why formatting in B2 was needed, and the tools used to format. |
| B2 done | 1 | The message is vague and does not provide enough context. It should describe what was done in B2 and what the objective of B2 is. |
| Implement the Create a ToDo user story:Implement NavigationImplement Firestore RepositoryImplement AddToDo logic and ScreensNeed to be fixed : ListToDoModel need to implement features of the ToDorepository, It's currenctly done in the ToDoAddScreen | 4 | The commit is clear and comprehensible. However, a link to the documentation would be helpful to understand the changes made. |
| Fix building error, formatted to ktmftFormat | 4 | The message is somewhat clear but it would be better to explain what the building error was and what was done to correct it. |
| Implement the Sign-in and LoginAdd written 2 user stories to userStories.txtImplement Greeting() methodPass all tests for B1 | 5 | The message is clear and explains what has been done. |
| week1- CI done | 1 | The message is vague and does not provide enough context. It does not use capitalized letters nor imperative, and does not dive any information on the modifications used to arrive at the CI done. |
| **Average commit messages score** | 3.6666666666666665 / 5 | |

</details>
