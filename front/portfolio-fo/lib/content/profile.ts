/**
 * Static personal/profile content. Not covered by portfolio-api-spec.md,
 * so it lives here as hand-edited content rather than an API call.
 */
export const profile = {
  name: '홍길동',
  role: 'Backend Developer',
  heroBio:
    'Spring Boot와 React 사이를 오가며, 기능 단위로 문제를 정의하고 해결합니다.',
  aboutIntro:
    'Spring Boot와 React 사이를 오가며, 기능 단위로 문제를 정의하고 해결하는 개발자입니다. 방문자용 화면과 관리자용 화면을 나누고, 직접 유지보수할 수 있는 구조를 만드는 데 관심이 있습니다.',
  experience: [
    {
      period: '2025 — 현재',
      title: '백엔드 개발자',
      org: '회사명',
      desc: '경력 내용이 이 자리에 들어갑니다.',
    },
  ],
  education: [
    {
      period: '2019 — 2025',
      title: '컴퓨터공학과 학사',
      org: '학교명',
    },
  ],
  contacts: [
    { label: 'EMAIL', value: 'hello@example.com', href: 'mailto:hello@example.com' },
    { label: 'GITHUB', value: 'github.com/username', href: '#' },
    { label: 'LINKEDIN', value: 'linkedin.com/in/username', href: '#' },
  ],
};
