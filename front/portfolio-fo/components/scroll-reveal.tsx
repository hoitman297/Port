'use client';

import { useEffect, useRef, useState, type ReactNode } from 'react';
import { animate } from 'animejs';

export function ScrollReveal({
  children,
  translateY = 24,
  duration = 700,
  delay = 0,
  className,
}: {
  children: ReactNode;
  translateY?: number;
  duration?: number;
  delay?: number;
  className?: string;
}) {
  const ref = useRef<HTMLDivElement>(null);
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setVisible(true);
          observer.disconnect();
        }
      },
      { threshold: 0.15 }
    );
    observer.observe(el);
    return () => observer.disconnect();
  }, []);

  useEffect(() => {
    if (!visible || !ref.current) return;
    animate(ref.current, {
      opacity: [0, 1],
      translateY: [translateY, 0],
      duration,
      delay,
      ease: 'outQuart',
    });
  }, [visible, translateY, duration, delay]);

  return (
    <div ref={ref} className={className} style={{ opacity: 0 }}>
      {children}
    </div>
  );
}
