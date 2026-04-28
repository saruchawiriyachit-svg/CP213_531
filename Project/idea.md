Project Proposal

Mobile Application: Decidr 

⸻

Branding Concept
	•	App Name: Decidr
	•	Creator: sarucha
	•	Mascot: Jelly (Golden Retriever 🐶)
	•	Concept:
“เพื่อนอัจฉริยะที่ช่วยคุณตัดสินใจ”

⸻

Step 1: Project Overview

1. ชื่อโครงการ

Decidr by sarucha
AI-powered Decision Support Mobile Application

⸻

2. ที่มาและความสำคัญของโครงการ

ในชีวิตประจำวัน ผู้คนต้องเผชิญกับการตัดสินใจอยู่เสมอ เช่น
	•	ควรซื้อของหรือไม่
	•	ควรเลือกทำงานชิ้นใดก่อน
	•	ควรเลือกตัวเลือกใดในหลายทางเลือก

แม้จะเป็นการตัดสินใจทั่วไป แต่เมื่อมีหลายปัจจัย เช่น ราคา เวลา ความคุ้มค่า และความรู้สึกส่วนตัว เข้ามาเกี่ยวข้อง มักทำให้เกิดความลังเลและใช้เวลานาน

แอป Decidr ถูกพัฒนาขึ้นเพื่อแก้ปัญหาดังกล่าว โดยใช้แนวคิด
	•	AI-assisted decision making
	•	Weighted scoring system
	•	Explainable AI

ร่วมกับการออกแบบ UX ที่เป็นมิตร โดยมีตัวละคร Jelly (สุนัขโกลเด้น) เป็นผู้ช่วยในการสื่อสารกับผู้ใช้

แอปนี้จึงไม่ใช่เพียงเครื่องมือช่วยเลือก แต่เป็น Intelligent & Personalized Decision Companion

⸻

3. วัตถุประสงค์
	•	พัฒนาแอป Android สำหรับช่วยตัดสินใจด้วย AI
	•	ฝึกใช้ Kotlin + Jetpack Compose
	•	ออกแบบระบบ Decision Engine
	•	เรียนรู้ MVVM Architecture
	•	พัฒนา Explainable AI และ Feedback Loop
	•	สร้างแอปที่มี UX/UI และ Branding ที่ชัดเจน

⸻

4. กลุ่มเป้าหมาย
	•	นักศึกษา
	•	คนวัยทำงาน (18–35 ปี)
	•	คนที่ตัดสินใจยาก
	•	ผู้ที่สนใจ AI และ productivity tools

⸻

Step 2: Tech Stack

Platform
	•	Android (Kotlin)
	•	Android Studio

UI
	•	Jetpack Compose
	•	Material 3

Architecture
	•	MVVM

Database
	•	Room Database

AI
	•	AI API (เช่น OpenAI)

Design
	•	Pastel UI + Mascot-driven UX

⸻

Step 3: System Design

Core Components

1. Decision Engine
	•	Weighted Scoring
	•	Factor-based evaluation

2. AI Reasoning Engine
	•	วิเคราะห์ pros/cons
	•	ให้ recommendation
	•	อธิบายเหตุผล

3. Feedback Loop 
	•	user กด 👍 / 👎
	•	ระบบเรียนรู้พฤติกรรม

4. Jelly Assistant 
	•	แสดงข้อความ
	•	guide user
	•	เพิ่ม personality

⸻

 Screen Structure

 Main Screen
	•	ใส่คำถาม
	•	เพิ่มตัวเลือก
	•	เพิ่ม factor + weight
	•	Quick Mode / Advanced Mode

⸻

 Result Screen
	•	แสดงคะแนน
	•	Highlight ตัวเลือกที่ดีที่สุด
	•	แสดงเหตุผล (Explainable AI)
	•	Jelly ให้คำแนะนำ

⸻

 Chat Screen
	•	ถามต่อ
	•	AI ตอบแบบ context-aware

⸻

 History Screen
	•	ดู decision ย้อนหลัง
	•	ดูผล + feedback

⸻

 Template Screen
	•	เลือกประเภทการตัดสินใจ
	•	auto-fill factor

⸻

 System Flow
	1.	ผู้ใช้สร้างคำถาม
	2.	เลือก Mode (Quick / Advanced)
	3.	ใส่ข้อมูล
	4.	ระบบคำนวณคะแนน
	5.	AI วิเคราะห์
	6.	แสดงผล + Jelly แนะนำ
	7.	user ให้ feedback
	8.	บันทึกข้อมูล

⸻

 Data Structure

Decision
	•	id
	•	title
	•	mode
	•	created_at

Option
	•	id
	•	decision_id
	•	name
	•	score

Factor
	•	id
	•	decision_id
	•	name
	•	weight

History
	•	id
	•	result
	•	confidence
	•	feedback
	•	timestamp

⸻

 Step 4: Functional Requirements

Core
	•	เพิ่มตัวเลือก
	•	เพิ่ม factor + weight
	•	วิเคราะห์ decision

AI
	•	วิเคราะห์ pros/cons
	•	แนะนำตัวเลือก
	•	อธิบายเหตุผล

Advanced
	•	Scenario simulation
	•	Chat follow-up
	•	Feedback learning

UX
	•	Jelly assistant
	•	Quick Mode
	•	Templates

⸻

 Step 5: Non-Functional Requirements
	•	ใช้งานง่าย
	•	ตอบสนองเร็ว
	•	UI น่ารัก + ทันสมัย
	•	Reliable
	•	ขยายระบบได้

⸻

 Step 6: Expected Outcomes
	•	ได้แอป Android ใช้งานจริง
	•	เข้าใจ AI integration
	•	ได้ portfolio ระดับสูง
	•	แสดงความสามารถด้าน product thinkin
