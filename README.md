# Interview Transcript Analysis - Engine

[![Actions Status](https://github.com/GaPhil/hackzurich-2019-engine/workflows/Lint/badge.svg)](https://github.com/GaPhil/hackzurich-2019-engine/actions)

Submission project for HackZürich 2019.

Team members: [Phillip Gajland](https://github.com/GaPhil) and [Simone Stefani](https://github.com/SimoneStefani)

### Challenge

Wouldn’t it be nice if working with recruitment firms was easier and more efficient for candidates? We all have CV’s, but we can’t have our life story on them else no one would read them. So you spend your time interviewing to give a fuller picture. However where there are humans, there is human error. Maybe the recruiter doesn’t update your file correctly, or misunderstands something and you have no chance to correct them after the interview.

The opportunity here is to create something that currently doesn’t exist. Use NLP tools to improve the chances of your interview landing you a relevant job, and quicker. Lots of information is exchanged between candidates and recruiters. This data, if well structured and accurate, can be very valuable. This is not an easy task, but if successful, you will improve the job search of every candidate we come across, including you. We will provide you with what you need to get going from the recruitment side, then together, we have the opportunity to change the world of recruitment for the better, forever. Data and NLP are already changing the face of many industries. Let’s shake up recruitment. It needs it.


### Endpoints

* GET: `/api/skills`
* POST: `/api/analyse`

## Submission

### Inspiration
The global job market is more competitive than ever! Whilst candidates continue to develop their skills, many of these do not show on their CVs or get lost in conversation during interviews.

### What it does
The Dream Engine extracts structured, valuable information from recorded job interviews (transcripts). The platform harnesses state of the art NLP along with our own algorithms and heuristics to give HR the insight they need, so that you can land the job you’ve been longing for!

### How we built it
The engine implements the Google Cloud Natural Language API. and key features include
* Element sentiment analysis to identify a candidate’s skills (e.g. A candidate might be proficient in "databases", "SQL" or "mySQL").
* Sentiment analysis and our own domain specific heuristics to quantify a candidate’s skill level. (e.g. A candidate may have "years of experience" in one area whilst only "a little" experience in another).
* Analysis of dependency trees to identify negation (e.g. A topic might come up in conversation although the candidate is not skilled at it).

### Challenges we ran into
Dealing with multiple edge cases and understanding the complexity of real time natural language processing.

### Accomplishments that we're proud of
We were able to dedicate our efforts to the challenges of NLP applied in a context with few data sets available, where standard machine learning can not be used. And not killing each other!

### What we learned
NLP techniques and a lot about the semantics of language.

### What's next for Dream Engine
Only time will tell, but Go hard, or home! - We went hard, now we're going home to Sweden.
