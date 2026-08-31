'use client';

import { useEffect, useRef } from 'react';
import { animate, stagger } from 'animejs';

export function HeroText({
  text,
  className,
  staggerMs = 40,
  startDelay = 0,
}: {
  text: string;
  className?: string;
  staggerMs?: number;
  startDelay?: number;
}) {
  const ref = useRef<HTMLSpanElement>(null);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    const chars = el.querySelectorAll('[data-char]');
    animate(chars, {
      opacity: [0, 1],
      translateY: ['0.5em', 0],
      delay: stagger(staggerMs, { start: startDelay }),
      duration: 700,
      ease: 'outExpo',
    });
  }, [staggerMs, startDelay]);

  return (
    <span ref={ref} className={className}>
      {Array.from(text).map((ch, i) => (
        <span key={i} data-char style={{ display: 'inline-block', opacity: 0 }}>
          {ch === ' ' ? ' ' : ch}
        </span>
      ))}
    </span>
  );
}
